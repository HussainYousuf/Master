package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    MyAdapter mAdapter;
    List<Trip> trips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //init
        Button driverTrip = findViewById(R.id.driverTrip);
        Button passengerTrip = findViewById(R.id.passengerTrip);
        trips = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(trips);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //listener
        driverTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Map1.class);
                intent.putExtra("type","driver");
                startActivity(intent);
                finish();
            }
        });
        passengerTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Map1.class);
                intent.putExtra("type","passenger");
                startActivity(intent);
                finish();
            }
        });
        getTrips();
    }


    void getTrips(){
        trips.clear();
        Map json = new HashMap();
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences sharedPref = getSharedPreferences("local", Context.MODE_PRIVATE);
        String _id = sharedPref.getString("_id", null);
        if(_id == null) {
            startActivity(new Intent(Home.this,Login.class));
            finish();
        }
        json.put("_id",_id);
        JSONObject parameters = new JSONObject(json);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"getTrips", parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray array = response.getJSONArray("trips");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = array.getJSONObject(i);
                        boolean isTypeDriver = temp.getString("type").equals("driver");
                        trips.add(new Trip(temp.getString("tripId"),temp.getString("originName"),temp.getString("destinationName"),temp.getBoolean("status"),isTypeDriver));
                    }
                    mAdapter.notifyDataSetChanged();

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
        Volley.newRequestQueue(Home.this).add(jsonRequest);
    }


    public class Trip {
        String tripId,originName,destinationName;
        boolean isTypeDriver,status;

        public Trip(String tripId, String originName, String destinationName, boolean status, boolean isTypeDriver) {
            this.tripId = tripId;
            this.originName = originName;
            this.destinationName = destinationName;
            this.status = status;
            this.isTypeDriver = isTypeDriver;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Trip> trips;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageButton delete;
            public TextView textView;
            public View view;
            public ImageView image;

            public MyViewHolder(View v) {
                super(v);
                textView = v.findViewById(R.id.textView);
                textView.setMovementMethod(new ScrollingMovementMethod());
                delete =  v.findViewById(R.id.delete);
                image = v.findViewById(R.id.image);
                view =  v;
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map json = new HashMap();
                        json.put("tripId",trips.get(MyViewHolder.this.getAdapterPosition()).tripId);
                        if(trips.get(MyViewHolder.this.getAdapterPosition()).isTypeDriver) json.put("type","driver");
                        else json.put("type","passenger");
                        JSONObject parameters = new JSONObject(json);
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"deleteTrip", parameters, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    response.getString("ok");
                                    getTrips();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        Volley.newRequestQueue(Home.this).add(jsonRequest);
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Trip trip = trips.get(MyViewHolder.this.getAdapterPosition());
                        String tripId = trip.tripId;
                        if(trip.isTypeDriver){
                            Intent intent = new Intent(Home.this,Routes.class);
                            intent.putExtra("tripId",tripId);
                            startActivity(intent);

                        }
                        if(!trip.isTypeDriver && trip.status){
                            Intent intent = new Intent(Home.this,Drivers.class);
                            intent.putExtra("tripId",tripId);
                            startActivity(intent);
                        }
                    }
                });

            }
        }

        public MyAdapter(List<Trip> myDataset) {
            trips = myDataset;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.textView.setText(trips.get(position).originName + "\nTo\n" + trips.get(position).destinationName);
            if(trips.get(position).isTypeDriver){
                TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.BLACK)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(50) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRect("DR", Color.TRANSPARENT);
                holder.image.setImageDrawable(drawable);
            }else {
                TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.BLACK)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(50) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRect("PA", Color.TRANSPARENT);
                holder.image.setImageDrawable(drawable);
            }
            if(trips.get(position).status)
                holder.view.setBackgroundColor(Color.rgb(200,230,200));
            else
                holder.view.setBackgroundColor(0);

        }

        @Override
        public int getItemCount() {
            return trips.size();
        }
    }
}
