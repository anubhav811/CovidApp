package com.anubhav.covidapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mAge,mAddress,mPhone;
    Button btn_register;
    Button logInButton;

    RadioGroup rg;
    RadioButton isPatient;
    RadioButton isDoctor;
    RadioButton isPharmacist;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAge       = findViewById(R.id.et_age);
        mFullName   = findViewById(R.id.et_name);
        mEmail      = findViewById(R.id.et_email);
        mPassword   = findViewById(R.id.et_password);
        mAddress   = findViewById(R.id.et_address);
        mPhone   = findViewById(R.id.et_phone);

        btn_register= findViewById(R.id.btn_register);
        logInButton   = findViewById(R.id.logInButton);

        rg = findViewById(R.id.rg);
        isPatient = findViewById(R.id.isPatient);
        isPharmacist = findViewById(R.id.isPharmacist);
        isDoctor = findViewById(R.id.isDoctor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),PatientActivity.class));
            finish();
             }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String name = mFullName.getText().toString().trim();
                final String age = mAge.getText().toString().trim();
                 String address = mAddress.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    mFullName.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(age)){
                    mAge.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                if(!isDoctor.isChecked() && !isPatient.isChecked() && !isPharmacist.isChecked()){
                    isPatient.setError("Select a user type ! ");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            FirebaseUser fUser = fAuth.getCurrentUser();
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("email",email);
                            userInfo.put("phone",phone);
                            userInfo.put("address",address);
                            userInfo.put("age",age);
                            if(isDoctor.isChecked()){
                                userInfo.put("isDoctor", "1");
                                startActivity(new Intent(getApplicationContext(),DoctorActivity.class));
                            }
                            else if (isPharmacist.isChecked()) {
                                userInfo.put("isPharmacist", "1");
                                startActivity(new Intent(getApplicationContext(),PharmacistActivity.class));
                            }
                            else if( isPatient.isChecked()){
                                userInfo.put("isPatient", "1");
                                startActivity(new Intent(getApplicationContext(),PatientActivity.class));
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
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}
