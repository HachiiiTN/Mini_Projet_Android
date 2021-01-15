package com.example.miniprojet.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.adapters.ClientsListAdapter;
import com.example.miniprojet.adapters.SitesListAdapter;
import com.example.miniprojet.managers.NewSiteManager;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Sites;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SiteActivity extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    // layouts
    private ListView sitesLv;
    private TextView siteClientName;
    private FloatingActionButton newSite;

    // vars
    private ArrayList<Sites> sitesList;
    private SitesListAdapter siteAdapter;
    private String clientName, clientCode;
    private Clients client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        // Action bar
        getSupportActionBar().setTitle("Sites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recover client data
        recoverClientData();

        // init firebase & layouts
        initFirebase();
        initLayout();

        // display and update clients list
        displaySitesList();

        newSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewSites = new Intent(SiteActivity.this, NewSiteManager.class);
                addNewSites.putExtra("clientName", clientName);
                addNewSites.putExtra("clientCode", clientCode);
                startActivity(addNewSites);
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

    // recover client data
    private void recoverClientData() {
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("client");
        client = gson.fromJson(strObj, Clients.class);
        assert client != null;
        clientName = client.getName();
        clientCode = client.getCode();
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
        siteClientName = findViewById(R.id.siteClientNameTxt);
        sitesLv = findViewById(R.id.sitesListView);
        newSite = findViewById(R.id.addNewSiteBtn);

        sitesList = new ArrayList<>();

        siteClientName.setText(clientName);
    }

    private void displaySitesList() {
        siteAdapter = new SitesListAdapter(SiteActivity.this, sitesList);
        myRef.child("Sites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sitesList.clear();
                for (DataSnapshot clientData : dataSnapshot.getChildren()) {
                    String clientKey = clientData.getKey();
                    assert clientKey != null;

                    if (clientKey.equals(clientName)) {
                        for (DataSnapshot siteData : clientData.getChildren()) {
                            Sites site = siteData.getValue(Sites.class);
                            assert site != null;
                            sitesList.add(site);
                            sitesLv.setAdapter(siteAdapter);
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