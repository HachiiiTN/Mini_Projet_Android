package com.example.miniprojet.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.managers.DetailManager;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;

public class DetailsFragment extends Fragment {

    private Clients client;
    private Interventions intervention;

    private TextView hourField, dateField, commentField, finishedField;
    private TextView clientContactField, clientField, clientEmailField, clientPhoneField;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        DetailManager activity = (DetailManager) getActivity();
        assert activity != null;
        intervention = activity.getInterventionData();
        client = activity.getInterventionClient();

        fillInterventionDetails(view);
        fillClientDetails(view);

        return view;
    }

    private void fillInterventionDetails(View view) {
        hourField = view.findViewById(R.id.detailHour);
        dateField = view.findViewById(R.id.detailDate);
        commentField = view.findViewById(R.id.detailComment);
        finishedField = view.findViewById(R.id.detailFinished);

        String time = "Time : " + intervention.getHeuredeb() + " - " + intervention.getHeurefin();
        String finished = (intervention.getTerminer()) ? "Yes" : "No";

        hourField.setText(time);
        dateField.setText("Date : " + intervention.getDatedeb());
        commentField.setText("Comments :\n" + intervention.getCommentaire());
        finishedField.setText("Finished : " + finished);
    }

    private void fillClientDetails(View view) {
        clientContactField = view.findViewById(R.id.detailClientContact);
        clientField = view.findViewById(R.id.detailClient);
        clientEmailField = view.findViewById(R.id.detailClientEmail);
        clientPhoneField = view.findViewById(R.id.detailClientPhone);

        clientContactField.setText("Contact Name : " + client.getContact());
        clientField.setText("Company : " + client.getId());
        clientEmailField.setText("Email : " + client.getEmail());
        clientPhoneField.setText("Phone : " + client.getPhone());
    }
}