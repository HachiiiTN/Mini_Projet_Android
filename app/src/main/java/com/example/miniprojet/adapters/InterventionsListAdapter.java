package com.example.miniprojet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.miniprojet.R;
import com.example.miniprojet.managers.AssignInterventionManager;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.utils.InterventionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InterventionsListAdapter extends ArrayAdapter<Interventions> {

    private Context context;
    private ArrayList<Interventions> interventionData;

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;

    // Layouts
    private TextView intervTitle, intervClient, intervSite, intervTime;
    private CheckBox intervCheckbox;

    // Vars
    private Interventions intervention;

    public InterventionsListAdapter(Context context, ArrayList<Interventions> interventionData) {
        super(context, R.layout.interventions_list_view, interventionData);
        this.context = context;
        this.interventionData = interventionData;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.interventions_list_view, parent, false);
        }

        intervTitle = view.findViewById(R.id.intervTitleView);
        intervClient = view.findViewById(R.id.intervClientNameView);
        intervSite = view.findViewById(R.id.intervClientAdrView);
        intervTime = view.findViewById(R.id.intervTimeView);
        intervCheckbox = view.findViewById(R.id.intervCheckBox);

        intervention = getItem(position);
        assert intervention != null;

        intervCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!intervCheckbox.isChecked()) {
                    myDatabase = FirebaseDatabase.getInstance();
                    myRef = myDatabase.getReference();
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    myRef = myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("terminer");
                                    myRef.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            intervCheckbox.setChecked(false);
                                        }
                                    });
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    myRef = myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("terminer");
                                    myRef.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            intervCheckbox.setChecked(true);
                                        }
                                    });
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure ?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } else {
                    myDatabase = FirebaseDatabase.getInstance();
                    myRef = myDatabase.getReference();
                    myRef = myRef.child("Interventions").child(intervention.getSiteId()).child(intervention.getId()).child("terminer");
                    myRef.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            intervCheckbox.setChecked(true);
                        }
                    });
                }
            }
        });

        String interventionTime = intervention.getHeuredeb() + " - " + intervention.getHeurefin();
        intervTitle.setText(intervention.getTitle());
        intervClient.setText(intervention.getClientId());
        intervSite.setText(intervention.getSiteId());
        intervTime.setText(interventionTime);
        intervCheckbox.setChecked(intervention.getTerminer());

        return view;
    }
}
