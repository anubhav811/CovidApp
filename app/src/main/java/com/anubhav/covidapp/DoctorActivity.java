package com.anubhav.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firestore.v1.Value;

import java.util.HashMap;
import java.util.Map;

public class DoctorActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView availablePatients;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    String docName;
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
    public void appointment(View view){
        Toast.makeText(getApplicationContext(),"Appointment Made",Toast.LENGTH_LONG).show();
        finish();
    }
    public void prescription(View view){
        Toast.makeText(getApplicationContext(),"Prescription Made",Toast.LENGTH_LONG).show();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        availablePatients = findViewById(R.id.availablePatients);
//        String userIdDoc = firebaseAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firebaseFirestore.collection("users").document(userIdDoc);
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//                String docName = (String) document.getString("name");
//            }
//        });
        Query query= firebaseFirestore.collection("users").whereEqualTo("isPatient","1");
        FirestoreRecyclerOptions<AvailablePatientModel> options = new FirestoreRecyclerOptions.Builder<AvailablePatientModel>()
                .setQuery(query,AvailablePatientModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<AvailablePatientModel, PatientViewHolder>(options) {
            @NonNull
            @Override
            public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
                return new PatientViewHolder(view);

            }
            @Override
            protected void onBindViewHolder(@NonNull PatientViewHolder patientViewHolder, int i, @NonNull AvailablePatientModel availablePatientModel) {
                patientViewHolder.name.setText("Name: "+availablePatientModel.getName());
                patientViewHolder.pulse.setText("Pulse: "+availablePatientModel.getPulse());
                patientViewHolder.o2.setText("Oxygen: "+availablePatientModel.getO2());
                patientViewHolder.temperature.setText("Temperature: "+availablePatientModel.getTemperature());
                patientViewHolder.lung_capacity.setText("Lung Capacity: "+availablePatientModel.getLung_capacity());
            }
        };
        availablePatients.setHasFixedSize(true);
        availablePatients.setLayoutManager(new LinearLayoutManager(this));
        availablePatients.setAdapter(adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();

    }@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    private class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView pulse;
        private TextView o2;
        private TextView lung_capacity;
        private TextView temperature;
        public PatientViewHolder(@NonNull View doctor_item_view) {
            super(doctor_item_view);
            name = doctor_item_view.findViewById(R.id.patient_name);
            pulse = doctor_item_view.findViewById(R.id.pulse);
            temperature = doctor_item_view.findViewById(R.id.temperature);
            o2 = doctor_item_view.findViewById(R.id.oxygen);
            lung_capacity = doctor_item_view.findViewById(R.id.lung_capacity);

        }
    }
}
