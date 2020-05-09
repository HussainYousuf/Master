package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drivers extends AppCompatActivity {

    List<Driver> drivers;
    MyAdapter mAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        getDrivers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers);
        drivers = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(drivers);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    void getDrivers(){
        drivers.clear();
        Map json = new HashMap();
        json.put("tripId",getIntent().getStringExtra("tripId"));
        JSONObject parameters = new JSONObject(json);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"getDrivers", parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("drivers");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = array.getJSONObject(i);
                        String driverTripId = temp.getString("tripId");
                        String driverRouteId = temp.getString("routeId");
                        String status = temp.getString("status");
                        String name = temp.getString("name");
                        String mobile = temp.getString("mobile");
                        String destination = temp.getString("destinationName");
                        String origin = temp.getString("originName");
                        String fare = temp.getString("fare");
                        String timing = temp.getString("timing");
                        drivers.add(new Driver(name,mobile,origin,destination,fare,timing,driverTripId,driverRouteId,status));
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
        Volley.newRequestQueue(Drivers.this).add(jsonRequest);
    }

    class Driver{

        String name,details,driverTripId,status,driverRouteId;

        public Driver(String name, String mobile, String origin, String destination, String fare, String timing, String driverTripId, String driverRouteId, String status) {
            this.name = name;
            this.details = "mobile: "+mobile+"\norigin: "+origin+"\ndestination: "+destination+"\nfare: "+fare+"\ntiming: "+timing;
            this.driverRouteId = driverRouteId;
            this.driverTripId = driverTripId;
            this.status = status;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Driver> drivers;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public Button btn;
            public TextView name;
            public TextView details;
            public View view;

            public MyViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                details = v.findViewById(R.id.details);
                btn = v.findViewById(R.id.btn);
                view = v;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status = drivers.get(MyViewHolder.this.getAdapterPosition()).status;
                        if(status.equals("requested") || status.equals("accepted")){
                            Map json = new HashMap();
                            json.put("passengerTripId",getIntent().getStringExtra("tripId"));
                            json.put("driverTripId",drivers.get(MyViewHolder.this.getAdapterPosition()).driverTripId);
                            json.put("driverRouteId",drivers.get(MyViewHolder.this.getAdapterPosition()).driverRouteId);
                            JSONObject parameters = new JSONObject(json);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"cancelRequest", parameters, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response.getString("ok");
                                        getDrivers();
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
                            Volley.newRequestQueue(Drivers.this).add(jsonRequest);
                        }else{
                            Map json = new HashMap();
                            json.put("tripId",getIntent().getStringExtra("tripId"));
                            json.put("driverTripId",drivers.get(MyViewHolder.this.getAdapterPosition()).driverTripId);
                            json.put("driverRouteId",drivers.get(MyViewHolder.this.getAdapterPosition()).driverRouteId);
                            JSONObject parameters = new JSONObject(json);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"sendRequest", parameters, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response.getString("ok");
                                        getDrivers();
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
                            Volley.newRequestQueue(Drivers.this).add(jsonRequest);
                        }
                    }
                });
            }
        }

        public MyAdapter(List<Driver> myDataset) {
            drivers = myDataset;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drivers_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.name.setText(drivers.get(position).name);
            holder.details.setText(drivers.get(position).details);
            final String status = drivers.get(position).status;
            if(status.equals("requested") || status.equals("accepted")) holder.btn.setText("cancel");
            else holder.btn.setText("request");
            if(status.equals("accepted")) holder.view.setBackgroundColor(Color.rgb(200,230,200));
            else holder.view.setBackgroundColor(0);
        }

        @Override
        public int getItemCount() {
            return drivers.size();
        }
    }

}


