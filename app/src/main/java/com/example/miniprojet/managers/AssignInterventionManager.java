package com.example.miniprojet.managers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Employees;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.utils.ClientActivity;
import com.example.miniprojet.utils.InterventionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignInterventionManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // layouts
    private Button assignToBtn;
    private AlertDialog alertDialog;
    private EditText assignTitleField, assignClientField, assignSiteField, assignDatedebField, assignDatefinField, assignHeuredebField, assignHeurefinField, assignCommentsField;
    private Spinner assignEmployeeSpinner;

    // vars
    private String employeeId;
    private ArrayList<String> employeesList;
    private ArrayAdapter<String> employeeAdapter;
    private String assignEmployee, intervId, intervTitle, intervClient, intervSite, intervDATDEB, intervDATFIN, intervHRDEB, intervHRFIN, intervComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_intervention_manager);

        // recover client data
        Bundle b = getIntent().getExtras();
        assert b != null;
        intervId = (String) b.get("interventionId");
        intervTitle = (String) b.get("interventionTitle");
        intervClient = (String) b.get("interventionClient");
        intervSite = (String) b.get("interventionSite");
        intervDATDEB = (String) b.get("interventionDATDEB");
        intervDATFIN = (String) b.get("interventionDATFIN");
        intervHRDEB = (String) b.get("interventionHRDEB");
        intervHRFIN = (String) b.get("interventionHRFIN");
        intervComments = (String) b.get("interventionComments");

        // Action bar
        getSupportActionBar().setTitle(intervId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        // fill employee spinner
        populateEmployeesList();

        assignToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignEmployee = assignEmployeeSpinner.getSelectedItem().toString();
                Toast.makeText(AssignInterventionManager.this, "id : " + employeeId, Toast.LENGTH_SHORT).show();
                assignToBtn.setEnabled(false);

                myRef = myRef.child("Interventions").child(intervSite).child(intervId).child("employeeId");
                myRef.setValue(employeeId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        displayAlertDialog(getString(R.string.intervention_successfully_assigned));
                        assignToBtn.setClickable(true);
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(AssignInterventionManager.this, InterventionActivity.class));
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
        employeesList = new ArrayList<>();

        assignTitleField = findViewById(R.id.intervAssignTitle);
        assignClientField = findViewById(R.id.intervAssignClient);
        assignSiteField = findViewById(R.id.intervAssignSite);
        assignDatedebField = findViewById(R.id.intervAssignDatedeb);
        assignDatefinField = findViewById(R.id.intervAssignDatefin);
        assignHeuredebField = findViewById(R.id.intervAssignHeuredeb);
        assignHeurefinField = findViewById(R.id.intervAssignHeurefin);
        assignCommentsField = findViewById(R.id.intervAssignComments);
        assignEmployeeSpinner = findViewById(R.id.intervAssignEmployeeSpinner);

        assignToBtn = findViewById(R.id.intervAssignBtn);

        assignTitleField.setEnabled(false);
        assignClientField.setEnabled(false);
        assignSiteField.setEnabled(false);
        assignDatedebField.setEnabled(false);
        assignDatefinField.setEnabled(false);
        assignHeuredebField.setEnabled(false);
        assignHeurefinField.setEnabled(false);
        assignCommentsField.setEnabled(false);

        assignTitleField.setText(intervTitle);
        assignClientField.setText(intervClient);
        assignSiteField.setText(intervSite);
        assignDatedebField.setText(intervDATDEB);
        assignDatefinField.setText(intervDATFIN);
        assignHeuredebField.setText(intervHRDEB);
        assignHeurefinField.setText(intervHRFIN);
        assignCommentsField.setText(intervComments);

        assignEmployeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myRef.child("Employees").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot employeeData : dataSnapshot.getChildren()) {
                            Employees employee = employeeData.getValue(Employees.class);
                            assert employee != null;
                            String fullName = employee.getNom() + " " + employee.getPrenom();
                            if (assignEmployeeSpinner.getSelectedItem().toString().equals(fullName)) {
                                employeeId = employee.getId();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(AssignInterventionManager.this).create();
        alertDialog.setTitle("Assign intervention");
        alertDialog.setMessage(string);
        alertDialog.show();
    }

    // Populate Employee dropdown list
    private void populateEmployeesList() {
        employeeAdapter = new ArrayAdapter<>(AssignInterventionManager.this, android.R.layout.simple_spinner_dropdown_item, employeesList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myRef.child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeesList.clear();
                for (DataSnapshot employeeData : dataSnapshot.getChildren()) {
                    Employees employee = employeeData.getValue(Employees.class);
                    assert employee != null;
                    String fullName = employee.getNom() + " " + employee.getPrenom();
                    employeesList.add(fullName);
                    assignEmployeeSpinner.setAdapter(employeeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}