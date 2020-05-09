package com.hussain_yousuf.wifip2pshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    // IO
    public static File dir;


    //Controls
    private TextView instructions;
    private TextView name;
    private TextView password;
    private TextView address;
    private Button btn;


    //static field shared by service
    public static AssetManager assetManager;

    //misc
    private BroadcastReceiver updateUIReciver;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-5892269477195375~9238521040");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5892269477195375/2354382647");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });





        //creating folders and files
        File root = android.os.Environment.getExternalStorageDirectory();
        dir = new File (root.getAbsolutePath() + "/WIFIP2P");
        if(!dir.exists()){
            dir.mkdirs();
        }
        assetManager = getAssets();

        //command backLight to remain on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //BroadCast
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyService.WIFI_INFO);
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ssid = intent.getStringExtra("ssid");
                String pass = intent.getStringExtra("pass");
                String add = intent.getStringExtra("address");
                name.setText("Name: "+ssid);
                password.setText("Password: "+pass);
                address.setText("Web-Address: "+add);


            }
        };
        registerReceiver(updateUIReciver,filter);

        // Components Initialization and action listeners

        name = (TextView) findViewById(R.id.name);
        password = (TextView) findViewById(R.id.password);
        address = (TextView) findViewById(R.id.address);
        btn = (Button) findViewById(R.id.btn);
        instructions = (TextView) findViewById(R.id.instructions);
        instructions.setMovementMethod(new ScrollingMovementMethod());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        unregisterReceiver(updateUIReciver);
    }

}
