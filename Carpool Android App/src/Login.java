package com.example.carpool;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    static String url = "http://192.168.1.20:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        //service
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("carpool", "carpool", importance);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            Intent pushIntent = new Intent(this, MyService.class);
//            startForegroundService(pushIntent);
//        } else {
//            Intent pushIntent = new Intent(this, MyService.class);
//            startService(pushIntent);
//        }
        //init
        final Button login = findViewById(R.id.login);
        final EditText name = findViewById(R.id.name);
        final EditText mobile = findViewById(R.id.mobile);
        final EditText password = findViewById(R.id.password);
        //listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.url);
                url = editText.getText().toString();
                Map json = new HashMap();
                json.put("name",name.getText().toString().trim());
                json.put("mobile",mobile.getText().toString().trim());
                json.put("password",password.getText().toString().trim());
                //remote
                JSONObject parameters = new JSONObject(json);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url+"login", parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String _id = response.getString("_id");
                            //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences sharedPref = getSharedPreferences("local", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("_id", _id);
                            editor.commit();
                            startActivity(new Intent(Login.this, Home.class));
                        } catch (Exception e) {
                            Toast.makeText(Login.this,"name exists try another",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                Volley.newRequestQueue(Login.this).add(jsonRequest);
            }
        });

    }
}
