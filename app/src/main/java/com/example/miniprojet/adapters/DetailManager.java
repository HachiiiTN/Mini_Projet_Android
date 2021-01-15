package com.example.miniprojet.adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // Layouts
    private TextView intervTitleField, intervClientField, intervTimeField, intervDateField;
    private Switch intervDoneSwitch;

    // Vars
    private Boolean interventionDone;
    private String intervId, employeeId, intervTitle, intervClient, intervSite, intervDATDEB, intervDATFIN, intervHRDEB, intervHRFIN, intervComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_manager);

        // recover client data
        Bundle b = getIntent().getExtras();
        assert b != null;
        intervId = (String) b.get("interventionId");
        employeeId = (String) b.get("employeeId");
        intervTitle = (String) b.get("interventionTitle");
        interventionDone = (Boolean) b.get("interventionDone");
        intervClient = (String) b.get("interventionClient");
        intervSite = (String) b.get("interventionSite");
        intervDATDEB = (String) b.get("interventionDATDEB");
        intervDATFIN = (String) b.get("interventionDATFIN");
        intervHRDEB = (String) b.get("interventionHRDEB");
        intervHRFIN = (String) b.get("interventionHRFIN");
        intervComments = (String) b.get("interventionComments");

        // Action bar
        getSupportActionBar().setTitle("Intervention Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();
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

        String time = intervHRDEB + " - " + intervHRFIN;
        intervTitleField.setText(intervTitle.toUpperCase());
        intervClientField.setText(intervClient);
        intervTimeField.setText(time);
        intervDateField.setText(intervDATDEB);
        intervDoneSwitch.setChecked(interventionDone);
    }
}
