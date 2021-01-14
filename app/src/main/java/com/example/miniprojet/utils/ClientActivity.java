package com.example.miniprojet.utils;

import android.content.Intent;
import android.os.Bundle;

import com.example.miniprojet.adapters.ClientsListAdapter;
import com.example.miniprojet.managers.NewClientManager;
import com.example.miniprojet.managers.NewSiteManager;
import com.example.miniprojet.models.Clients;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.miniprojet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    // layouts
    private ListView clientsLv;
    private FloatingActionButton newClient;

    // vars
    private ArrayList<Clients> clientsList;
    private ClientsListAdapter clientAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Action bar
        getSupportActionBar().setTitle("Clients");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        // display clients list
        displayClientsList();

        clientsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                Intent goToSites = new Intent(ClientActivity.this, SiteActivity.class);
                goToSites.putExtra("clientName", clientsList.get(i).getId());
                goToSites.putExtra("clientCode", clientsList.get(i).getCode());
                startActivity(goToSites);
            }
        });

        newClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientActivity.this, NewClientManager.class));
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
        clientsLv = findViewById(R.id.clientsListView);
        newClient = findViewById(R.id.addNewClientBtn);

        clientsList = new ArrayList<>();
    }

    private void displayClientsList() {
        clientAdapter = new ClientsListAdapter(ClientActivity.this, clientsList);
        myRef.child("Clients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    Clients client = data.getValue(Clients.class);
                    assert client != null;
                    clientsList.add(client);
                    clientsLv.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}