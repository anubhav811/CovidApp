package com.anubhav.covidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    public static final String TAG = "TAG";
    String userID;
    EditText mFullName,mEmail,mPassword,mAge,mPhone;
    Button btn_register;

    RadioButton isPatient;
    RadioButton isDoctor;
    RadioButton isPharmacist;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_register,container,false);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mAge       = root.findViewById(R.id.et_age);
        mFullName   = root.findViewById(R.id.et_name);
        mEmail      = root.findViewById(R.id.et_email);
        mPassword   = root.findViewById(R.id.et_password);
        mPhone   = root.findViewById(R.id.et_phone);

        btn_register= root.findViewById(R.id.btn_register);

        isPatient = root.findViewById(R.id.isPatient);
        isPharmacist = root.findViewById(R.id.isPharmacist);
        isDoctor = root.findViewById(R.id.isDoctor);
        if(fAuth.getCurrentUser() != null){
            Intent intent = new Intent(getActivity(),PatientActivity.class);
            startActivity(intent);
        }

        btn_register.setOnClickListener(v -> {
            final String email = mEmail.getText().toString().trim();
            final String name = mFullName.getText().toString().trim();
            final String age = mAge.getText().toString().trim();
            final String phone = mPhone.getText().toString().trim();
            final String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(name)){
                mFullName.setError("Name is Required.");
                return;
            }
            if(TextUtils.isEmpty(age)){
                mAge.setError("Age is Required.");
                return;
            }
            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required.");
                return;
            }
            if(TextUtils.isEmpty(phone)){
                mPassword.setError("Phone is Required.");
                return;
            }
            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password is Required.");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("Password Must be >= 6 Characters");
                return;
            }

            if(!isDoctor.isChecked() && !isPatient.isChecked() && !isPharmacist.isChecked()){
                isPatient.setError("Select a user type !");
                return;
            }

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    FirebaseUser fUser = fAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),"Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                    Toast.makeText(getActivity(), "User Created", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String,Object> userInfo = new HashMap<>();
                    userInfo.put("name",name);
                    userInfo.put("email",email);
                    userInfo.put("phone",phone);
                    userInfo.put("age",age);
                    if(isDoctor.isChecked()){
                        userInfo.put("isDoctor", "1");
                        Intent intent = new Intent(getActivity(),DoctorActivity.class);
                        startActivity(intent);
                    }
                    else if (isPharmacist.isChecked()) {
                        userInfo.put("isPharmacist", "1");
                        Intent intent = new Intent(getActivity(),PharmacistActivity.class);
                        startActivity(intent);
                    }
                    else if( isPatient.isChecked()){
                        userInfo.put("isPatient", "1");
                        Intent intent = new Intent(getActivity(),PatientActivity.class);
                        startActivity(intent);
                    }
                    documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });


                }
                else {
                    Toast.makeText(getActivity(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        });
        return root;
    }
}