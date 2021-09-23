package com.anubhav.covidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    EditText mEmail,mPassword;
    Button login_btn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fireStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        fAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        mEmail      = root.findViewById(R.id.et_email);
        mPassword   = root.findViewById(R.id.et_password);
        login_btn   = root.findViewById(R.id.login_btn);



        if (fAuth.getCurrentUser() != null) {
            DocumentReference df = fireStore.collection("users").document(fAuth.getCurrentUser().getUid());
            df.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.getString("isDoctor") != null) {
                    startActivity(new Intent(getActivity(), DoctorActivity.class));
                } else if (documentSnapshot.getString("isPatient") != null) {
                    startActivity(new Intent(getActivity(), PatientActivity.class));
                } else if (documentSnapshot.getString("isPharmacist") != null) {
                    startActivity(new Intent(getActivity(), PharmacistActivity.class));
                }
            });
        }

        login_btn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required.");
                return;
            }

            if (password.length() < 6) {
                mPassword.setError("Password Must be >= 6 Characters");
                return;
            }

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fireStore.collection("users").document(fAuth.getCurrentUser().getUid());
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getString("isDoctor") != null) {
                                startActivity(new Intent(getActivity(), DoctorActivity.class));
                            } else if (documentSnapshot.getString("isPatient") != null) {
                                startActivity(new Intent(getActivity(), PatientActivity.class));
                            } else if (documentSnapshot.getString("isPharmacist") != null) {
                                startActivity(new Intent(getActivity(), PharmacistActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "This user does not exist", Toast.LENGTH_SHORT).show();

                }
            });
        });
        return root;
    }
    }