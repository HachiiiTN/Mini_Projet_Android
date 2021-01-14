package com.example.miniprojet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Interventions;

import java.util.ArrayList;

public class InterventionsListAdapter extends ArrayAdapter<Interventions> {

    private Context context;
    private ArrayList<Interventions> interventionData;

    public InterventionsListAdapter(Context context, ArrayList<Interventions> interventionData) {
        super(context, R.layout.interventions_list_view, interventionData);
        this.context = context;
        this.interventionData = interventionData;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.interventions_list_view, parent, false);
        }

        TextView intervTitle = view.findViewById(R.id.intervTitleView);
        TextView intervClient = view.findViewById(R.id.intervClientNameView);
        TextView intervClientAdr = view.findViewById(R.id.intervClientAdrView);
        TextView intervTime = view.findViewById(R.id.intervTimeView);
        CheckBox intervCheckbox = view.findViewById(R.id.intervCheckBox);

        Interventions intervention = getItem(position);
        assert intervention != null;

        String interventionTime = intervention.getHeuredeb() + " - " + intervention.getHeurefin();

        intervTitle.setText(intervention.getTitle());
        intervClient.setText(intervention.getClientId());
        intervClientAdr.setText(intervention.getSiteId());
        intervTime.setText(interventionTime);
        intervCheckbox.setChecked(false);

        return view;
    }
}
