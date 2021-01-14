package com.example.miniprojet.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.example.miniprojet.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Action bar
        getSupportActionBar().setTitle("Historique");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}