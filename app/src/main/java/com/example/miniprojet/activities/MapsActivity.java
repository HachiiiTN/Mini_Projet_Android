package com.example.miniprojet.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.miniprojet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String client, site, longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // recover longitude & altitude
        Bundle b = getIntent().getExtras();
        client = (String) b.get("client");
        site = (String) b.get("site");
        longitude = (String) b.get("longitude");
        latitude = (String) b.get("latitude");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(17);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude));
        mMap.addMarker(new MarkerOptions().position(location).title(client));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));

    }
}