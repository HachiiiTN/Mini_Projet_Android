package com.example.miniprojet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Clients;
import com.example.miniprojet.models.Sites;

import java.util.ArrayList;

public class SitesListAdapter extends ArrayAdapter<Sites> {

    private Context context;
    private ArrayList<Sites> sitesData;

    public SitesListAdapter(Context context, ArrayList<Sites> sitesData) {
        super(context, R.layout.simple_custom_list_view, sitesData);
        this.sitesData = sitesData;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.simple_custom_list_view, parent, false);
        }

        TextView clientNameText = view.findViewById(R.id.firstTextView);
        TextView siteAddressText = view.findViewById(R.id.secondTextView);
        TextView siteVilleText = view.findViewById(R.id.thirdTextView);
        TextView siteRueText = view.findViewById(R.id.fourthTextView);

        Sites site = getItem(position);
        assert site != null;

        clientNameText.setText(site.getId());
        siteAddressText.setText(site.getAddress());
        siteVilleText.setText(site.getVille());
        siteRueText.setText(site.getRue());

        return view;
    }
}
