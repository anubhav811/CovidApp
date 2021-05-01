package com.anubhav.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientActivity extends AppCompatActivity {
    Button sendToDoc;
    Button getVitalsButton;
    LinearLayout vitals;
    TextView o2;
    TextView pulse ;
    TextView temperature;
    TextView lung_cap;
    Button seeAppointments;
    FirebaseAuth firebaseAuth;
    TextView appointmentText;
    FirebaseFirestore firebaseFirestore;
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
    public void getVitals(View view){
        vitals.setVisibility(View.INVISIBLE);
        o2.setText("Oxygen Level: 98 %");
        pulse.setText("Pulse : 70 bpm");
        temperature.setText("Temperature : 101 °F");
        lung_cap.setText("Lung Capacity : 90 %");
        vitals.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        sendToDoc = findViewById(R.id.sendToDoc);
        o2 = findViewById(R.id.o2);
        pulse = findViewById(R.id.pulse);
        temperature = findViewById(R.id.temperature);
        lung_cap = findViewById(R.id.lung_cap);
        vitals = findViewById(R.id.vitals);
        getVitalsButton = findViewById(R.id.getVitalsButton);
        vitals.setVisibility(View.INVISIBLE);
        seeAppointments = findViewById(R.id.seeAppointments);
        appointmentText = findViewById(R.id.appointmentText);
        sendToDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AvailableDoctors.class));
            }
        });
        seeAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();
                String userIdDoc = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userIdDoc);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String docName = (String) document.getString("docSelected");
                        String appointmentTime = (String) document.getString("appointmentTime");
                        String appointmentDate = (String) document.getString("appointmentDate");
                        String oxygenLevel = (String)document.get("o2");
                        String pulseLevel = (String)document.get("pulse");
                        String temperatureValue = (String)document.get("temperature");
                        String lung_capacity = (String)document.get("lung_capacity");


                        appointmentText.setText("Ashok Kumar :  At 4:20 PM on 04/05/21");
                        seeAppointments.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
}
}