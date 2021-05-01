package com.anubhav.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AvailableDoctors extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView availableDocList;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_doctors);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        availableDocList = findViewById(R.id.availableDocList);
        Query query= firebaseFirestore.collection("users").whereEqualTo("isDoctor","1");
        FirestoreRecyclerOptions<DoctorsModel> options = new FirestoreRecyclerOptions.Builder<DoctorsModel>()
                .setQuery(query,DoctorsModel.class)
                .build();
         adapter = new FirestoreRecyclerAdapter<DoctorsModel, DoctorViewHolder>(options) {
            @NonNull
            @Override
            public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_doc_items,parent,false);
                return new DoctorViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorViewHolder doctorViewHolder, int i, @NonNull DoctorsModel doctorsModel) {
                    doctorViewHolder.name.setText(doctorsModel.getName());
                    doctorViewHolder.address.setText(doctorsModel.getAddress());


            }
        };
         availableDocList.setHasFixedSize(true);
         availableDocList.setLayoutManager(new LinearLayoutManager(this));
         availableDocList.setAdapter(adapter);
        }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView address;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.docName);
            address = itemView.findViewById(R.id.docAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AvailableDoctors.this,"Your Report has been successfully sent",Toast.LENGTH_LONG).show();
                    String doctor = ((TextView) availableDocList.findViewHolderForAdapterPosition(getAdapterPosition()).itemView.findViewById(R.id.docName)).getText().toString();
                    String userIDpatient = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userIDpatient);
                    Map<String,Object> userInfo = new HashMap<>();
                    userInfo.put("docSelected",doctor);
                    documentReference.update(userInfo);
                }
            });
        }
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
//    public void sendDoc(View view){
//    userIDpatient = firebaseAuth.getCurrentUser().getUid();
//
//}

}

//                    Intent intent = new Intent(getApplicationContext(),DoctorActivity.class);
//                    intent.putExtra("name", (Bundle) userInfo.get("name"));
//                    intent.putExtra("age", (Bundle) userInfo.get("age"));
//                    intent.putExtra("o2", (Bundle) userInfo.get("o2"));
//                    intent.putExtra("pulse", (Bundle) userInfo.get("pulse"));
//                    intent.putExtra("lung_cap", (Bundle) userInfo.get("lung_cap"));
//                    intent.putExtra("temperature", (Bundle) userInfo.get("temperature"));