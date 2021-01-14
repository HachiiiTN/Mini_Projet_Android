package com.example.miniprojet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Clients;

import java.util.ArrayList;

public class ClientsListAdapter extends ArrayAdapter<Clients> {

    private Context context;
    private ArrayList<Clients> clientsData;


    public ClientsListAdapter(Context context, ArrayList<Clients> clientsData) {
        super(context, R.layout.simple_custom_list_view, clientsData);
        this.clientsData = clientsData;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.simple_custom_list_view, parent, false);
        }

        TextView clientNameText = view.findViewById(R.id.firstTextView);
        TextView clientAddressText = view.findViewById(R.id.secondTextView);
        TextView clientEmailAdrText = view.findViewById(R.id.thirdTextView);
        TextView clientPhoneText = view.findViewById(R.id.fourthTextView);

        Clients client = getItem(position);
        assert client != null;

        clientNameText.setText(client.getName());
        clientAddressText.setText(client.getAddress());
        clientEmailAdrText.setText(client.getEmail());
        clientPhoneText.setText(client.getPhone());

        return view;

    }
}
