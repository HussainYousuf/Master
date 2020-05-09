package com.example.carpool;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Map2 extends FragmentActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map2);
        //init
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        List<LatLng> latLngs = PolyUtil.decode(Routes.currentRoute.polyline);
        mMap.addPolyline(new PolylineOptions().addAll(latLngs));
        LatLng curLocation = latLngs.get(0);
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(curLocation,10);
        mMap.moveCamera(camera);
        for (Routes.Passenger passenger: Routes.currentRoute.passengers) {
            if(passenger.status.equals("accepted")){
                mMap.addMarker(new MarkerOptions().position(passenger.origin).title(passenger.name));
                mMap.addMarker(new MarkerOptions().position(passenger.destination).title(passenger.name));
            }
        }
    }
}
