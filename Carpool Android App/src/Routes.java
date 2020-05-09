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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes extends AppCompatActivity {

    static Route currentRoute;
    TabLayout tabLayout;
    ArrayList<Route> routes;
    int prevTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes);

        tabLayout = findViewById(R.id.tabLayout);

        routes = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        getRoutes();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = (Integer) tab.getTag();
                prevTab = i;
                currentRoute = routes.get(i);
                MyAdapter mAdapter = new MyAdapter(currentRoute.passengers);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                TextView summary = findViewById(R.id.summary);
                summary.setText(currentRoute.summary);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Routes.this,Map2.class));
            }
        });

    }

    private void getRoutes() {
        routes.clear();
        tabLayout.removeAllTabs();
        Map json = new HashMap();
        json.put("tripId",getIntent().getStringExtra("tripId"));
        JSONObject parameters = new JSONObject(json);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"getRoutes", parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("routes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = array.getJSONObject(i);
                        String routeId = temp.getString("routeId");
                        String summary = temp.getString("summary");
                        String polyline = temp.getString("polyline");
                        JSONArray array2 = temp.getJSONArray("passengers");
                        List<Passenger> passengers = new ArrayList<>();
                        for (int j = 0; j < array2.length(); j++) {
                            temp = array2.getJSONObject(j);
                            String name = temp.getString("name");
                            String mobile = temp.getString("mobile");
                            double destinationLat = temp.getDouble("destinationLat");
                            double originLat = temp.getDouble("originLat");
                            double destinationLng = temp.getDouble("destinationLng");
                            double originLng = temp.getDouble("originLng");
                            String destinationName = temp.getString("destinationName");
                            String originName = temp.getString("originName");
                            String passengerTripId = temp.getString("passengerTripId");
                            String status = temp.getString("status");
                            passengers.add(new Passenger(name,mobile,originName,destinationName,new LatLng(originLat,originLng),new LatLng(destinationLat,destinationLng),passengerTripId,status));
                        }
                        routes.add(new Route(routeId,summary,polyline,passengers));
                    }
                    for (int i = 0; i < routes.size(); i++) {
                        TabLayout.Tab tab = tabLayout.newTab().setText("Route"+(i+1)).setTag(i);
                        tabLayout.addTab(tab);
                        if(i == prevTab) tab.select();
                    }

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
        Volley.newRequestQueue(Routes.this).add(jsonRequest);
    }

    class Route{
        String routeId,summary,polyline;
        List<Passenger> passengers;

        public Route(String routeId, String summary, String polyline, List<Passenger> passengers) {
            this.routeId = routeId;
            this.summary = summary;
            this.polyline = polyline;
            this.passengers = passengers;
        }
    }

    class Passenger{

        String name,details,passengerTripId,status;
        LatLng origin,destination;

        public Passenger(String name, String mobile, String originName, String destinationName, LatLng origin, LatLng destination, String passengerTripId, String status) {
            this.name = name;
            this.details = "mobile: "+mobile+"\norigin: "+originName+"\ndestination: "+destinationName;
            this.origin = origin;
            this.destination = destination;
            this.passengerTripId = passengerTripId;
            this.status = status;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Passenger> passengers;

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
                        String status = passengers.get(MyViewHolder.this.getAdapterPosition()).status;
                        if(status.equals("accepted")){
                            Map json = new HashMap();
                            json.put("driverTripId",getIntent().getStringExtra("tripId"));
                            json.put("passengerTripId",passengers.get(MyViewHolder.this.getAdapterPosition()).passengerTripId);
                            json.put("driverRouteId",currentRoute.routeId);
                            JSONObject parameters = new JSONObject(json);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"cancelRequest", parameters, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response.getString("ok");
                                        getRoutes();
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
                            Volley.newRequestQueue(Routes.this).add(jsonRequest);
                        }else{
                            Map json = new HashMap();
                            json.put("tripId",getIntent().getStringExtra("tripId"));
                            json.put("passengerTripId",passengers.get(MyViewHolder.this.getAdapterPosition()).passengerTripId);
                            json.put("routeId",currentRoute.routeId);
                            JSONObject parameters = new JSONObject(json);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Login.url+"acceptRequest", parameters, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response.getString("ok");
                                        getRoutes();
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
                            Volley.newRequestQueue(Routes.this).add(jsonRequest);
                        }
                    }
                });
            }
        }

        public MyAdapter(List<Passenger> myDataset) {
            passengers = myDataset;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.routes_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.name.setText(passengers.get(position).name);
            holder.details.setText(passengers.get(position).details);
            final String status = passengers.get(position).status;
            if(status.equals("accepted"))  {
                holder.btn.setText("cancel");
                holder.view.setBackgroundColor(Color.rgb(200,230,200));
            }else{
                holder.btn.setText("accept");
                holder.view.setBackgroundColor(0);
            }
        }

        @Override
        public int getItemCount() {
            return passengers.size();
        }
    }

}


