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

public class AssignIntervsListAdapter extends ArrayAdapter<Interventions> {

    private Context context;
    private ArrayList<Interventions> interventionData;

    public AssignIntervsListAdapter(Context context, ArrayList<Interventions> interventionData) {
        super(context, R.layout.interventions_list_view, interventionData);
        this.context = context;
        this.interventionData = interventionData;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.simple_custom_list_view, parent, false);
        }

        TextView intervTitle = view.findViewById(R.id.firstTextView);
        TextView intervClient = view.findViewById(R.id.secondTextView);
        TextView intervClientAdr = view.findViewById(R.id.thirdTextView);
        TextView intervTime = view.findViewById(R.id.fourthTextView);

        Interventions intervention = getItem(position);
        assert intervention != null;

        String interventionTime = intervention.getHeuredeb() + " - " + intervention.getHeurefin();

        intervTitle.setText(intervention.getTitle());
        intervClient.setText(intervention.getClientId());
        intervClientAdr.setText(intervention.getSiteId());
        intervTime.setText(interventionTime);

        return view;
    }
}
