package com.example.miniprojet.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.adapters.ClientsListAdapter;
import com.example.miniprojet.adapters.InterventionsListAdapter;
import com.example.miniprojet.managers.NewInterventionManager;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InterventionActivity extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    // layouts
    private ListView intervLv;
    private FloatingActionButton newInterv;

    // vars
    private ArrayList<Interventions> intervsList;
    private InterventionsListAdapter interventionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention);

        // Action bar
        getSupportActionBar().setTitle("à Assigner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        // display interventions list
        displayInterventionsList();

        newInterv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InterventionActivity.this, NewInterventionManager.class));
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // init firebase
    private void initFirebase() {
        myDatabase = FirebaseDatabase.getInstance();
        myRef = myDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
    }

    // init layouts
    private void initLayout() {
        intervLv = findViewById(R.id.interventionsListView);
        newInterv = findViewById(R.id.addNewIntervBtn);

        intervsList = new ArrayList<>();
    }

    private void displayInterventionsList() {
        interventionAdapter = new InterventionsListAdapter(InterventionActivity.this, intervsList);
        myRef.child("Interventions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                intervsList.clear();
                for (DataSnapshot siteData : dataSnapshot.getChildren())
                {
                    for (DataSnapshot intervData : siteData.getChildren()) {
                        Interventions intervention = intervData.getValue(Interventions.class);
                        assert intervention != null;
                        if (intervention.getEmployeeId().equals("None")) {
                            intervsList.add(intervention);
                            intervLv.setAdapter(interventionAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}