package com.example.miniprojet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.activities.MapsActivity;
import com.example.miniprojet.managers.DetailManager;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Interventions;
import com.example.miniprojet.models.Sites;

public class DetailsFragment extends Fragment {

    private Sites site;
    private Clients client;
    private Interventions intervention;

    private TextView hourField, dateField, commentField, finishedField;
    private TextView clientContactField, clientField, clientEmailField, clientPhoneField, siteAddress;
    private Button mapsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        DetailManager activity = (DetailManager) getActivity();
        assert activity != null;
        intervention = activity.getInterventionData();
        client = activity.getInterventionClient();
        site = activity.getInterventionSite();

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
        siteAddress = view.findViewById(R.id.detailSiteAddress);
        mapsButton = view.findViewById(R.id.detailMapBtn);

        clientContactField.setText("Contact Name : " + client.getContact());
        clientField.setText("Company : " + client.getId());
        clientEmailField.setText("Email : " + client.getEmail());
        clientPhoneField.setText("Phone : " + client.getPhone());
        siteAddress.setText("Address : " + site.getAddress());

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps = new Intent(getContext(), MapsActivity.class);
                maps.putExtra("client", client.getId());
                maps.putExtra("site", site.getId());
                maps.putExtra("longitude", site.getLongitude());
                maps.putExtra("latitude", site.getLatitude());
                startActivity(maps);
            }
        });
    }
}