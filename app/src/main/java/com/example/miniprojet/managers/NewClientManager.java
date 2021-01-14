package com.example.miniprojet.managers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.utils.ClientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewClientManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // layouts
    private Button addClientBtn;
    private AlertDialog alertDialog;
    private EditText clientNameField, clientCodeField, clientAddressField, clientPhoneField, clientFaxField, clientEmailField, clientContactField, clientContactPhoneField;

    // vars
    private String clientName, clientCode, clientAddress, clientPhone, clientFax, clientEmail, clientContact, clientContactPhone;

    public NewClientManager() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        // Action bar
        getSupportActionBar().setTitle("New client");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientName = clientNameField.getText().toString();
                clientCode = clientCodeField.getText().toString();
                clientAddress = clientAddressField.getText().toString();
                clientPhone = clientPhoneField.getText().toString();
                clientFax = clientFaxField.getText().toString();
                clientEmail = clientEmailField.getText().toString();
                clientContact = clientContactField.getText().toString();
                clientContactPhone = clientContactPhoneField.getText().toString();

                if (TextUtils.isEmpty(clientName)) {
                    displayAlertDialog(getString(R.string.empty_client_name));
                    return;
                } else if (TextUtils.isEmpty(clientCode)) {
                    displayAlertDialog(getString(R.string.empty_client_name));
                    return;
                } else if (TextUtils.isEmpty(clientAddress)) {
                    displayAlertDialog(getString(R.string.empty_client_address));
                    return;
                } else if (TextUtils.isEmpty(clientPhone)) {
                    displayAlertDialog(getString(R.string.empty_client_phone));
                    return;
                } else if (TextUtils.isEmpty(clientFax)) {
                    displayAlertDialog(getString(R.string.empty_client_fax));
                    return;
                } else if (TextUtils.isEmpty(clientEmail)) {
                    displayAlertDialog(getString(R.string.empty_client_email));
                    return;
                } else if (TextUtils.isEmpty(clientContact)) {
                    displayAlertDialog(getString(R.string.empty_client_contact));
                    return;
                } else if (TextUtils.isEmpty(clientContactPhone)) {
                    displayAlertDialog(getString(R.string.empty_client_contactPhone));
                    return;
                } else if (clientPhone.length() < 8) {
                    displayAlertDialog(getString(R.string.invalid_client_phone));
                    return;
                } else if (clientFax.length() < 8) {
                    displayAlertDialog(getString(R.string.invalid_client_fax));
                    return;
                } else if (clientContactPhone.length() < 8) {
                    displayAlertDialog(getString(R.string.invalid_client_contactPhone));
                    return;
                }

                addClientBtn.setClickable(false);

                myRef = myRef.child("Clients").child(clientName);
                Clients client = new Clients(myRef.getKey(), clientName, clientCode, clientAddress, clientPhone, clientFax, clientEmail, clientContact, clientContactPhone);
                myRef.setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        displayAlertDialog(getString(R.string.client_successfully_added));
                        addClientBtn.setClickable(true);
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(NewClientManager.this, ClientActivity.class));
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
        clientNameField = findViewById(R.id.clientNameEdit);
        clientCodeField = findViewById(R.id.clientCodeEdit);
        clientAddressField = findViewById(R.id.clientAddressEdit);
        clientPhoneField = findViewById(R.id.clientTelephoneEdit);
        clientFaxField = findViewById(R.id.clientFaxEdit);
        clientEmailField = findViewById(R.id.clientEmailEdit);
        clientContactField = findViewById(R.id.clientContactEdit);
        clientContactPhoneField = findViewById(R.id.clientContactTelEdit);

        addClientBtn = findViewById(R.id.addClientBtn);
    }

    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(NewClientManager.this).create();
        alertDialog.setTitle("Add new client");
        alertDialog.setMessage(string);
        alertDialog.show();
    }
}