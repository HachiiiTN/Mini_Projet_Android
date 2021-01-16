package com.example.miniprojet.managers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.miniprojet.R;
import com.example.miniprojet.adapters.ClientsListAdapter;
import com.example.miniprojet.adapters.MyPagerAdapter;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.utils.ClientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DetailManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // Layouts
    private TextView intervTitleField, intervClientField, intervTimeField, intervDateField;
    private Switch intervDoneSwitch;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;

    // Vars
    private Clients interventionClient;
    private Interventions intervention;
    private Boolean interventionDone;
    private String intervId, employeeId, intervTitle, intervClient, intervSite, intervDATDEB, intervDATFIN, intervHRDEB, intervHRFIN, intervComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_manager);

        // recover client data
        recoverInterventionData();

        // Action bar
        getSupportActionBar().setTitle("Intervention Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        getClientDataFromDatabase();

        intervDoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (intervDoneSwitch.isChecked()) {
                    myRef = myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("terminer");
                    myRef.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(DetailManager.this, "Finished has been updated to TRUE", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    myRef = myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("terminer");
                    myRef.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(DetailManager.this, "Finished has been updated to FALSE", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
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
    }

    // init layouts
    private void initLayout() {
        intervTitleField = findViewById(R.id.detailIntervTitle);
        intervClientField = findViewById(R.id.detailIntervClient);
        intervTimeField = findViewById(R.id.detailIntervTime);
        intervDateField = findViewById(R.id.detailIntervDate);
        intervDoneSwitch = findViewById(R.id.detailIntervSwitch);

        // update fields
        String time = intervHRDEB + " - " + intervHRFIN;
        intervTitleField.setText(intervTitle.toUpperCase());
        intervClientField.setText(intervClient);
        intervTimeField.setText(time);
        intervDateField.setText(intervDATDEB);
        intervDoneSwitch.setChecked(interventionDone);

        // tablayout and pager
        initTabLayoutsAndPager();
    }

    private void initTabLayoutsAndPager() {
        // tabLayout and pager
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Files"));
        tabLayout.addTab(tabLayout.newTab().setText("Signatures"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // recover intervention data
    private void recoverInterventionData() {
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("intervention");
        intervention = gson.fromJson(strObj, Interventions.class);

        intervTitle = intervention.getId();
        intervClient = intervention.getClientId();
        intervDATDEB = intervention.getDatedeb();
        intervHRDEB = intervention.getHeuredeb();
        intervHRFIN = intervention.getHeurefin();
        interventionDone = intervention.getTerminer();
    }

    // recover client data
    private void getClientDataFromDatabase() {
        interventionClient = new Clients();
        myRef.child("Clients").child(intervention.getClientId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Clients client = dataSnapshot.getValue(Clients.class);
                interventionClient = client;
                Log.e("TAG", interventionClient.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // send intervention data to tabLayout fragments
    public Interventions getInterventionData() {
        return intervention;
    }

    public Clients getInterventionClient() {
        return interventionClient;
    }

}
