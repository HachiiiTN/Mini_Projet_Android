package com.example.miniprojet.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.miniprojet.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Action bar
        getSupportActionBar().setTitle("About");

        String infos = "©2021 KHEMIRI Hassen";
        TextView infoAbout = findViewById(R.id.aboutInfo);
        infoAbout.setText(infos);
    }
}