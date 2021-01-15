package com.example.miniprojet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.adapters.DetailManager;
import com.example.miniprojet.adapters.InterventionsListAdapter;
import com.example.miniprojet.models.Employees;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.utils.AboutActivity;
import com.example.miniprojet.utils.ClientActivity;
import com.example.miniprojet.utils.InterventionActivity;
import com.example.miniprojet.utils.SettingsActivity;
import com.example.miniprojet.utils.SiteActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // layouts
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ListView intervListView;

    // vars
    private ArrayList<Interventions> intervsList;
    private InterventionsListAdapter interventionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Action bar
        getSupportActionBar().setTitle("Interventions");

        // init firebase & layouts
        initFirebase();
        initLayout();

        // drawer navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final View drawerView = navigationView.getHeaderView(0);
        myRef.child("Employees/" + mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Employees employee = dataSnapshot.getValue(Employees.class);
                assert employee != null;
                String employeeName = employee.getPrenom() + " " + employee.getNom().toUpperCase();
                ((TextView)drawerView.findViewById(R.id.drawerEmployeeName)).setText(employeeName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // display interventions assigned to logged user
        displayInterventionsList();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) { drawer.closeDrawer(GravityCompat.START); }
        else { super.onBackPressed(); }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            // Core menu
            case R.id.nav_inter_assign : startActivity(new Intent(MainActivity.this, InterventionActivity.class)); break;
            case R.id.nav_client : startActivity(new Intent(MainActivity.this, ClientActivity.class)); break;

            // Settings menu
            case R.id.nav_settings : startActivity(new Intent(MainActivity.this, SettingsActivity.class)); break;
            case R.id.nav_propos : startActivity(new Intent(MainActivity.this, AboutActivity.class)); break;
            case R.id.nav_signout : signOut(); break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // init firebase
    private void initFirebase() {
        myDatabase = FirebaseDatabase.getInstance();
        myRef = myDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // auth listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    // init layouts
    private void initLayout() {
        intervsList = new ArrayList<>();
        intervListView = findViewById(R.id.homeListView);

        intervListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                Intent goToDetails = new Intent(MainActivity.this, DetailManager.class);
                goToDetails.putExtra("interventionId", intervsList.get(i).getId());
                goToDetails.putExtra("employeeId", mAuth.getUid());
                goToDetails.putExtra("interventionTitle", intervsList.get(i).getTitle());
                goToDetails.putExtra("interventionClient", intervsList.get(i).getClientId());
                goToDetails.putExtra("interventionSite", intervsList.get(i).getSiteId());
                goToDetails.putExtra("interventionDone", intervsList.get(i).getTerminer());
                goToDetails.putExtra("interventionDATDEB", intervsList.get(i).getDatedeb());
                goToDetails.putExtra("interventionDATFIN", intervsList.get(i).getDatefin());
                goToDetails.putExtra("interventionHRDEB", intervsList.get(i).getHeuredeb());
                goToDetails.putExtra("interventionHRFIN", intervsList.get(i).getHeurefin());
                goToDetails.putExtra("interventionComments", intervsList.get(i).getCommentaire());
                startActivity(goToDetails);
            }
        });
    }

    private void signOut() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        mAuth.signOut();
        finish();
    }

    private void displayInterventionsList() {
        interventionAdapter = new InterventionsListAdapter(MainActivity.this, intervsList);
        myRef.child("Interventions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                intervsList.clear();
                for (DataSnapshot siteData : dataSnapshot.getChildren())
                {
                    for (DataSnapshot intervData : siteData.getChildren()) {
                        Interventions intervention = intervData.getValue(Interventions.class);
                        assert intervention != null;
                        if (intervention.getEmployeeId().equals(mAuth.getUid())) {
                            intervsList.add(intervention);
                            intervListView.setAdapter(interventionAdapter);
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