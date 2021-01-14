package com.example.miniprojet.managers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.activities.MainActivity;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewInterventionManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // layouts
    private DatePickerDialog.OnDateSetListener selectedDate;
    private Calendar calendar;
    private Button addInterventionBtn;
    private AlertDialog alertDialog;
    private EditText intervTitleField, intervDatedebField, intervDatefinField, intervHeuredebField, intervHeurefinField, intervCommentsField;
    private Spinner intervClientSpinner, intervSiteSpinner;

    // vars
    private ArrayList<String> clientsList, sitesList;
    private ArrayAdapter<String> clientAdapter, siteAdapter;
    private String intervTitle, intervDatedeb, intervDatefin, intervHeureDeb, intervHeurefin, intervComments;
    private String intervClient, intervSite, intervEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intervention);

        // Action bar
        getSupportActionBar().setTitle("New intervention");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        addInterventionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervTitle = intervTitleField.getText().toString();
                intervDatedeb = intervDatedebField.getText().toString();
                intervDatefin = intervDatefinField.getText().toString();
                intervHeureDeb = intervHeuredebField.getText().toString();
                intervHeurefin = intervHeurefinField.getText().toString();
                intervComments = intervCommentsField.getText().toString();
                intervClient = intervClientSpinner.getSelectedItem().toString();
                intervSite = intervSiteSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(intervTitle)) {
                    displayAlertDialog(getString(R.string.empty_intervention_title));
                    return;
                } else if (TextUtils.isEmpty(intervDatedeb)) {
                    displayAlertDialog(getString(R.string.empty_intervention_date_debut));
                    return;
                } else if (TextUtils.isEmpty(intervDatefin)) {
                    displayAlertDialog(getString(R.string.empty_intervention_date_fin));
                    return;
                } else if (TextUtils.isEmpty(intervHeureDeb)) {
                    displayAlertDialog(getString(R.string.empty_intervention_heure_debut));
                    return;
                } else if (TextUtils.isEmpty(intervHeurefin)) {
                    displayAlertDialog(getString(R.string.empty_intervention_heure_fin));
                    return;
                } else if (TextUtils.isEmpty(intervSite)) {
                    displayAlertDialog(getString(R.string.empty_intervention_site));
                    return;
                } else if (intervDatedeb.compareTo(intervDatefin) > 0) {
                    displayAlertDialog(getString(R.string.empty_intervention_invalid_start_date));
                    return;
                } else if (intervDatefin.compareTo(intervDatedeb) < 0) {
                    displayAlertDialog(getString(R.string.empty_intervention_invalid_end_date));
                    return;
                }

                addInterventionBtn.setClickable(true);

                DateFormat ndf = new SimpleDateFormat("MMddyy-HHmm");
                String postDate = ndf.format(Calendar.getInstance().getTime());
                String interventionId = "INTR" + postDate;

                myRef = myRef.child("Interventions").child(intervSite).child(interventionId);
                Interventions intervention = new Interventions(interventionId, intervTitle, intervDatedeb, intervDatefin, intervHeureDeb, intervHeurefin, intervComments, null, false);
                intervention.setEmployeeId("None");
                intervention.setClientId(intervClient);
                intervention.setSiteId(intervSite);

                myRef.setValue(intervention).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        displayAlertDialog(getString(R.string.intervention_successfully_added));
                        addInterventionBtn.setClickable(true);
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(NewInterventionManager.this, MainActivity.class));
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
        clientsList = new ArrayList<>();
        sitesList = new ArrayList<>();

        calendar = Calendar.getInstance();

        intervTitleField = findViewById(R.id.intervTitleEdit);
        intervDatedebField = findViewById(R.id.intervDatedebEdit);
        intervDatefinField = findViewById(R.id.intervDatefinEdit);
        intervHeuredebField = findViewById(R.id.intervHeureDebEdit);
        intervHeurefinField = findViewById(R.id.intervHeureFinEdit);
        intervCommentsField = findViewById(R.id.intervCommentsEdit);
        intervClientSpinner = findViewById(R.id.intervClientSpinner);
        intervSiteSpinner = findViewById(R.id.intervSiteSpinner);

        addInterventionBtn = findViewById(R.id.addIntervBtn);

        intervDatedebField.setKeyListener(null);
        intervDatefinField.setKeyListener(null);
        intervHeuredebField.setKeyListener(null);
        intervHeurefinField.setKeyListener(null);

        populateClients();
        intervClientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                intervSiteSpinner.setAdapter(null);
                populateSites();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateFieldListener(intervDatedebField);
        dateFieldListener(intervDatefinField);
        heureFieldListener(intervHeuredebField);
        heureFieldListener(intervHeurefinField);
    }

    // display calender on date debut field selection
    private void dateFieldListener(final EditText dateField) {
        final DatePickerDialog.OnDateSetListener selectedDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                dateField.setText(sdf.format(calendar.getTime()));
            }
        };

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewInterventionManager.this, selectedDate,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // display calendar on hour debut field selection
    private void heureFieldListener(final EditText hourField) {
        hourField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(NewInterventionManager.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String formattedHour = (selectedHour < 10) ? "0" + selectedHour : String.valueOf(selectedHour);
                        String formattedMinute = (selectedMinute < 10) ? "0" + selectedMinute : String.valueOf(selectedMinute);
                        String formattedTime = formattedHour + ":" + formattedMinute;
                        hourField.setText(formattedTime);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
    }

    // Return today's date
    private String getTodayDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        return sdf.format(calendar.getTime());
    }

    // display alert dialog
    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(NewInterventionManager.this).create();
        alertDialog.setTitle("Add new intervention");
        alertDialog.setMessage(string);
        alertDialog.show();
    }

    // Populate Sites dropdown list
    private void populateClients() {
        clientAdapter = new ArrayAdapter<>(NewInterventionManager.this, android.R.layout.simple_spinner_dropdown_item, clientsList);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myRef.child("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsList.clear();
                for (DataSnapshot clientData : dataSnapshot.getChildren()) {
                    Clients client = clientData.getValue(Clients.class);
                    assert client != null;
                    clientsList.add(client.getId());
                    intervClientSpinner.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Populate Sites dropdown list
    private void populateSites() {
        siteAdapter = new ArrayAdapter<>(NewInterventionManager.this, android.R.layout.simple_spinner_dropdown_item, sitesList);
        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myRef.child("Sites").child(intervClientSpinner.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sitesList.clear();
                for (DataSnapshot siteData : dataSnapshot.getChildren()) {
                        Sites site = siteData.getValue(Sites.class);
                        assert site != null;
                        sitesList.add(site.getId());
                        intervSiteSpinner.setAdapter(siteAdapter);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}