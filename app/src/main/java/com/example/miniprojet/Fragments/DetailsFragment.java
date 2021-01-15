package com.example.miniprojet.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.managers.DetailManager;
import com.example.miniprojet.models.Interventions;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        DetailManager activity = (DetailManager) getActivity();
        Interventions intervention = activity.getInterventionId();

        //TextView textView = view.findViewById(R.id.textViewID);
        //textView.setText(intervention.getId());

        return view;
    }
}