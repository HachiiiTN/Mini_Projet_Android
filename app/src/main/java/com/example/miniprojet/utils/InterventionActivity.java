package com.example.miniprojet.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.activities.MainActivity;
import com.example.miniprojet.adapters.AssignIntervsListAdapter;
import com.example.miniprojet.adapters.ClientsListAdapter;
import com.example.miniprojet.adapters.InterventionsListAdapter;
import com.example.miniprojet.managers.AssignInterventionManager;
import com.example.miniprojet.managers.NewInterventionManager;
import com.example.miniprojet.managers.NewSiteManager;
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
    private AssignIntervsListAdapter interventionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention);

        // Action bar
        getSupportActionBar().setTitle("To be assigned");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        // display interventions list
        displayInterventionsList();

        intervLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent assignIntervention = new Intent(InterventionActivity.this, AssignInterventionManager.class);
                assignIntervention.putExtra("interventionId", intervsList.get(i).getId());
                assignIntervention.putExtra("interventionTitle", intervsList.get(i).getTitle());
                assignIntervention.putExtra("interventionClient", intervsList.get(i).getClientId());
                assignIntervention.putExtra("interventionSite", intervsList.get(i).getSiteId());
                assignIntervention.putExtra("interventionDATDEB", intervsList.get(i).getDatedeb());
                assignIntervention.putExtra("interventionDATFIN", intervsList.get(i).getDatefin());
                assignIntervention.putExtra("interventionHRDEB", intervsList.get(i).getHeuredeb());
                assignIntervention.putExtra("interventionHRFIN", intervsList.get(i).getHeurefin());
                assignIntervention.putExtra("interventionComments", intervsList.get(i).getCommentaire());
                startActivity(assignIntervention);
            }
        });

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
        interventionAdapter = new AssignIntervsListAdapter(InterventionActivity.this, intervsList);
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