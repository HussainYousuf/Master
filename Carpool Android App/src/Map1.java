package com.example.carpool;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Map1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    public boolean isDone;
    public LatLng originLatLng,destinationLatLng;
    public String originName,destinationName;
    public String timing, fare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map1);
        //init
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Places.initialize(getApplicationContext(), "AIzaSyCRR9Ml2eCb4Fx4lAPKVCExkZnTvNArB14");
        Places.createClient(this);
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete);
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setCountry("PK");
        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(23.958, 66.401),new LatLng(25.593, 68.576)));
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setHint("Enter origin");
        Toast.makeText(this,"Enter origin",Toast.LENGTH_SHORT).show();
        //listener
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15));
                if(!isDone) originName = place.getName();
                else destinationName = place.getName();
            }
            @Override
            public void onError(Status status) {

            }
        });
        final Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isDone){
                    isDone = true;
                    originLatLng = marker.getPosition();
                    autocompleteFragment.setHint("Enter destination");
                    autocompleteFragment.setText("");
                    Toast.makeText(Map1.this,"Enter destination",Toast.LENGTH_SHORT).show();
                }else{
                    final String type = getIntent().getStringExtra("type");
                    destinationLatLng = marker.getPosition();
                    if(type.equals("driver")){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Map1.this);
                        builder1.setMessage("Enter trip time and your fare");
                        builder1.setCancelable(true);
                        View view = getLayoutInflater().inflate(R.layout.dialog, null);
                        builder1.setView(view);
                        final TimePicker picker = view.findViewById(R.id.timePicker);
                        final EditText fare = view.findViewById(R.id.fare);
                        builder1.setPositiveButton(
                                "Done",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        timing = picker.getCurrentHour()+":"+picker.getCurrentMinute();
                                        Map1.this.fare =  fare.getText().toString();
                                        addTrip(type);
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                    }else{
                        addTrip(type);
                    }
                }
            }
        });
        final Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDone){
                    startActivity(new Intent(Map1.this, Home.class));
                    finish();
                }else{
                    isDone = false;
                    autocompleteFragment.setHint("Enter origin");
                    autocompleteFragment.setText("");
                    Toast.makeText(Map1.this,"Enter origin",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void addTrip(String type)  {
        Geocoder geocoder = new Geocoder(this);
        try {
            if(originName == null){
                Address address = geocoder.getFromLocation(originLatLng.latitude,originLatLng.longitude,1).get(0);
                originName = address.getAddressLine(address.getMaxAddressLineIndex());
            }
            if(destinationName == null){
                Address address = geocoder.getFromLocation(destinationLatLng.latitude,destinationLatLng.longitude,1).get(0);
                destinationName = address.getAddressLine(address.getMaxAddressLineIndex());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap params = new HashMap();
        SharedPreferences sharedPref = getSharedPreferences("local", Context.MODE_PRIVATE);
        String _id = sharedPref.getString("_id", null);
        params.put("originLat",originLatLng.latitude);
        params.put("originLng",originLatLng.longitude);
        params.put("destinationLat",destinationLatLng.latitude);
        params.put("destinationLng",destinationLatLng.longitude);
        params.put("originName",originName);
        params.put("destinationName",destinationName);
        params.put("_id",_id);
        params.put("type",type);
        if(type.equals("driver")) {
            params.put("timing",timing);
            params.put("fare",fare);
        }
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"addTrip", parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.getString("ok");
                    startActivity(new Intent(Map1.this, Home.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(Map1.this).add(jsonRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LatLng curLocation = new LatLng(24.8607, 67.0011);
        marker = mMap.addMarker(new MarkerOptions()
                .position(curLocation));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(curLocation,10);
        mMap.moveCamera(camera);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                marker.setPosition(mMap.getCameraPosition().target);
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                marker.setPosition(mMap.getCameraPosition().target);
            }
        });
    }
}
