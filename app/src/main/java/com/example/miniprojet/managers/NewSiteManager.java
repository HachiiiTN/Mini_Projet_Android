package com.example.miniprojet.managers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Sites;
import com.example.miniprojet.utils.ClientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewSiteManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // layouts
    private Button addSiteBtn;
    private AlertDialog alertDialog;
    private EditText siteClientField, siteAddressField, siteVilleField, siteRueField, siteCodePostalField;
    private EditText siteLongitudeField, siteLatitudeField, siteContactField, siteContactPhoneField;

    // vars
    private String clientName, clientCode;
    private String siteContact, siteContactPhone, siteAddress, siteVille, siteRue, siteCodePostal, siteLongitude, siteLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_site);

        // Action bar
        getSupportActionBar().setTitle("New site");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recover client data
        Bundle b = getIntent().getExtras();
        assert b != null;
        clientName = (String) b.get("clientName");
        clientCode = (String) b.get("clientCode");

        // init firebase & layouts
        initFirebase();
        initLayout();

        addSiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siteAddress = siteAddressField.getText().toString();
                siteContact = siteContactField.getText().toString();
                siteContactPhone = siteContactPhoneField.getText().toString();
                siteVille = siteVilleField.getText().toString();
                siteRue = siteRueField.getText().toString();
                siteCodePostal = siteCodePostalField.getText().toString();
                siteLongitude = siteLongitudeField.getText().toString();
                siteLatitude = siteLatitudeField.getText().toString();

                if (TextUtils.isEmpty(siteContact)) {
                    displayAlertDialog(getString(R.string.empty_site_contact));
                    return;
                } else if (TextUtils.isEmpty(siteContactPhone)) {
                    displayAlertDialog(getString(R.string.empty_site_contactPhone));
                    return;
                } else if (TextUtils.isEmpty(siteVille)) {
                    displayAlertDialog(getString(R.string.empty_site_ville));
                    return;
                } else if (TextUtils.isEmpty(siteRue)) {
                    displayAlertDialog(getString(R.string.empty_site_rue));
                    return;
                } else if (TextUtils.isEmpty(siteCodePostal)) {
                    displayAlertDialog(getString(R.string.empty_site_codepostal));
                    return;
                } else if (TextUtils.isEmpty(siteLongitude)) {
                    displayAlertDialog(getString(R.string.empty_site_longitude));
                    return;
                } else if (TextUtils.isEmpty(siteLatitude)) {
                    displayAlertDialog(getString(R.string.empty_site_latitude));
                    return;
                } else if (siteCodePostal.length() < 4) {
                    displayAlertDialog(getString(R.string.invalid_site_codepostal));
                    return;
                } else if (siteContactPhone.length() != 8) {
                    displayAlertDialog(getString(R.string.invalid_site_contactPhone));
                    return;
                }

                addSiteBtn.setClickable(false);

                myRef = myRef.child("Sites").child(clientName).child(clientCode + " - " + siteVille);
                Sites site = new Sites(myRef.getKey(), siteLongitude, siteLatitude, siteAddress, siteRue, siteCodePostal, siteVille, siteContact, siteContactPhone);

                myRef.setValue(site).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        displayAlertDialog(getString(R.string.site_successfully_added));
                        addSiteBtn.setClickable(true);
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(NewSiteManager.this, ClientActivity.class));
                                finish();
                            }
                        }.start();
                    }
                });
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
        siteClientField = findViewById(R.id.siteClientEdit);
        siteContactField = findViewById(R.id.siteContactEdit);
        siteContactPhoneField = findViewById(R.id.siteContactPhoneEdit);
        siteAddressField = findViewById(R.id.siteAddressEdit);
        siteVilleField = findViewById(R.id.siteVilleEdit);
        siteRueField = findViewById(R.id.siteRueEdit);
        siteCodePostalField = findViewById(R.id.siteCodePostalEdit);
        siteLongitudeField = findViewById(R.id.siteLongitudeEdit);
        siteLatitudeField = findViewById(R.id.siteLatitudeEdit);

        addSiteBtn = findViewById(R.id.addSiteBtn);

        siteClientField.setEnabled(false);
        siteClientField.setText(clientName);
    }

    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(NewSiteManager.this).create();
        alertDialog.setTitle("Add new site");
        alertDialog.setMessage(string);
        alertDialog.show();
    }

    /*
    // Populate Clients dropdown list
    private void populateClients() {
        clientAdapter = new ArrayAdapter<>(NewSiteManager.this, android.R.layout.simple_spinner_dropdown_item, clientsList);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myRef.child("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsList.clear();
                for (DataSnapshot clientData : dataSnapshot.getChildren()) {
                    Clients client = clientData.getValue(Clients.class);
                    assert client != null;
                    clientsList.add(client.getName());
                    siteClientSpinner.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
     */
}