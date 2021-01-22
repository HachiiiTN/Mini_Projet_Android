package com.example.miniprojet.managers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojet.R;
import com.example.miniprojet.activities.MainActivity;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.models.Sites;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditInterventionManager extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // layouts
    private DatePickerDialog.OnDateSetListener selectedDate;
    private Calendar calendar;
    private Button editInterventionBtn;
    private AlertDialog alertDialog;
    private EditText intervTitleField, intervDatedebField, intervDatefinField, intervHeuredebField, intervHeurefinField, intervCommentsField;

    // vars
    private Interventions intervention;
    private ArrayList<String> clientsList, sitesList;
    private ArrayAdapter<String> clientAdapter, siteAdapter;
    private String intervTitle, intervDatedeb, intervDatefin, intervHeureDeb, intervHeurefin, intervComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_intervention);

        // Action bar
        getSupportActionBar().setTitle("Edit intervention");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recover intervention data
        recoverInterventionData();

        // init firebase & layouts
        initFirebase();
        initLayout();


        editInterventionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervTitle = intervTitleField.getText().toString();
                intervDatedeb = intervDatedebField.getText().toString();
                intervDatefin = intervDatefinField.getText().toString();
                intervHeureDeb = intervHeuredebField.getText().toString();
                intervHeurefin = intervHeurefinField.getText().toString();
                intervComments = intervCommentsField.getText().toString();

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
                } else if (intervDatedeb.compareTo(intervDatefin) > 0) {
                    displayAlertDialog(getString(R.string.empty_intervention_invalid_start_date));
                    return;
                } else if (intervDatefin.compareTo(intervDatedeb) < 0) {
                    displayAlertDialog(getString(R.string.empty_intervention_invalid_end_date));
                    return;
                }

                editInterventionBtn.setClickable(true);

                DateFormat ndf = new SimpleDateFormat("MMddyy-HHmm");
                String postDate = ndf.format(Calendar.getInstance().getTime());

                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("title").setValue(intervTitle);
                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("datedeb").setValue(intervDatedeb);
                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("datefin").setValue(intervDatefin);
                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("heuredeb").setValue(intervHeureDeb);
                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("heurefin").setValue(intervHeurefin);
                myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("commentaire").setValue(intervComments);

                Gson gson = new Gson();
                Intent goToDetails = new Intent(EditInterventionManager.this, DetailManager.class);
                goToDetails.putExtra("intervention", gson.toJson(intervention));
                startActivity(goToDetails);
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
    }

    // init layouts
    private void initLayout() {
        clientsList = new ArrayList<>();
        sitesList = new ArrayList<>();

        calendar = Calendar.getInstance();

        intervTitleField = findViewById(R.id.intervTitleEdit2);
        intervDatedebField = findViewById(R.id.intervDatedebEdit2);
        intervDatefinField = findViewById(R.id.intervDatefinEdit2);
        intervHeuredebField = findViewById(R.id.intervHeureDebEdit2);
        intervHeurefinField = findViewById(R.id.intervHeureFinEdit2);
        intervCommentsField = findViewById(R.id.intervCommentsEdit2);

        editInterventionBtn = findViewById(R.id.editIntervBtn);

        intervDatedebField.setKeyListener(null);
        intervDatefinField.setKeyListener(null);
        intervHeuredebField.setKeyListener(null);
        intervHeurefinField.setKeyListener(null);

        dateFieldListener(intervDatedebField);
        dateFieldListener(intervDatefinField);
        heureFieldListener(intervHeuredebField);
        heureFieldListener(intervHeurefinField);

        intervTitleField.setText(intervention.getTitle());
        intervDatedebField.setText(intervention.getDatedeb());
        intervDatefinField.setText(intervention.getDatefin());
        intervHeuredebField.setText(intervention.getHeuredeb());
        intervHeurefinField.setText(intervention.getHeurefin());
        intervCommentsField.setText(intervention.getCommentaire());
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
                new DatePickerDialog(EditInterventionManager.this, selectedDate,
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

                TimePickerDialog timePicker = new TimePickerDialog(EditInterventionManager.this, new TimePickerDialog.OnTimeSetListener() {
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
        alertDialog = new AlertDialog.Builder(EditInterventionManager.this).create();
        alertDialog.setTitle("Edit intervention");
        alertDialog.setMessage(string);
        alertDialog.show();
    }

    // recover intervention data
    private void recoverInterventionData() {
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("intervention");
        intervention = gson.fromJson(strObj, Interventions.class);
    }
}