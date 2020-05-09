package com.example.carpool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent stopSelf = new Intent(this, MyService.class);
        stopSelf.setAction(this.ACTION_STOP_SERVICE);
        PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "carpool")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("CarPoolService")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_delete_black_24dp, "Stop Service", pStopSelf);

        final NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "carpool")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);



        startForeground(1337, builder.build());

        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Map json = new HashMap();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String _id = sharedPref.getString("_id", null);
                json.put("_id",_id);
                final JSONObject parameters = new JSONObject(json);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Login.url+"notifications", parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray notifications = response.getJSONArray("notifications");
                            String notification = "";
                            for (int i = 0; i < notifications.length(); i++) {
                                notification += notifications.getString(i) + "\n";
                            }
                            builder2.setContentText(notification);
                            notificationManager.notify(1, builder2.build());
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
                Volley.newRequestQueue(MyService.this).add(jsonRequest);

            }
        },0,10, TimeUnit.SECONDS);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SERIVCE","on destroy called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
