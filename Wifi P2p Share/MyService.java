package com.hussain_yousuf.wifip2pshare;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.hussain_yousuf.wifip2pshare.MainActivity.dir;

public class MyService extends Service {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    public static final String WIFI_INFO = "WIFIINFO";
    private MyHttpServer server;
    private final int PORT = 8080;
    private Reciever reciever;
    private boolean running = true;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent stopIntent = new Intent();
        stopIntent.setAction("STOPSERVICE");

        PendingIntent stopThisService = PendingIntent.getBroadcast(this,1,stopIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("STOPSERVICE");

        reciever = new Reciever();

        registerReceiver(reciever,intentFilter);

        startWifiP2P();


        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("WifiP2p Service Started")
                .setContentText("Ask users to scan for wifi")
                .addAction(R.drawable.stop ,"Stop Service",stopThisService)
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_NOT_STICKY;

    }



    private void startWifiP2P() {

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(mReceiver, mIntentFilter);
    }

    public void createGroup(){

        destroyGroup();

        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason){
            }
        });

    }

    private void destroyGroup(){

        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i) {
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if(server != null) {
            server.closeAllConnections();
            server.stop();
        }
        destroyGroup();
        running = false;
        unregisterReceiver(reciever);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //BROADCASTS
    class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;

        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel) {
            super();
            this.mManager = manager;
            this.mChannel = channel;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    createGroup();
                } else {
                    WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                createGroup();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
                //server specific
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                final WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                if (networkInfo.isConnected() && wifiP2pInfo.groupFormed) {
                    if (wifiP2pInfo.isGroupOwner) {
                        final String IPADDRESS = wifiP2pInfo.groupOwnerAddress.toString().substring(1);
                        try {
                            server = new MyHttpServer(IPADDRESS,PORT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                            @Override
                            public void onGroupInfoAvailable(final WifiP2pGroup wifiP2pGroup) {
                                if (wifiP2pGroup != null) {
                                    String ssid = wifiP2pGroup.getNetworkName();
                                    String passphrase = wifiP2pGroup.getPassphrase();
                                    String fullAddress = IPADDRESS.trim() + ":8080";
                                    Intent intent = new Intent();
                                    intent.putExtra("ssid",ssid);
                                    intent.putExtra("pass",passphrase);
                                    intent.putExtra("address",fullAddress);
                                    intent.setAction(WIFI_INFO);
                                    sendBroadcast(intent);
                                }
                            }
                        });
                        mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                            @Override
                            public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo) {

                            }
                        });
                    }
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            }

        }

    }// End of WIFIP2PBROADCAST

    class MyHttpServer extends NanoWSD {

        private ConcurrentHashMap<String,ArrayList<MyWebSocket>> connections = new ConcurrentHashMap<String,ArrayList<MyWebSocket>>();

        public MyHttpServer(String ip,int port) throws IOException {
            super(ip,port);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(running){
                        if(!connections.keySet().isEmpty()) {
                            for (String group : connections.keySet()) {
                                for (MyWebSocket ws : connections.get(group)) {
                                    try {
                                        ws.ping("I".getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            start();
        }

        private String getMimeType(String fileUrl) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        public ConcurrentHashMap<String,ArrayList<MyWebSocket>> getConnections() {
            return this.connections;
        }

        @Override
        protected WebSocket openWebSocket(IHTTPSession handshake) {
            return new MyWebSocket(handshake, this);
        }

        @Override
        public Response serveHttp(IHTTPSession session) {


                InputStream is = null;

                if (session.getUri().startsWith("/download")) {
                    String fileName = session.getUri().substring(10);
                    try {
                        File file = new File(dir, fileName);
                        String MIME = getMimeType(file.getAbsolutePath());
                        FileInputStream fis = new FileInputStream(file);
                        NanoHTTPD.Response res = newChunkedResponse(Response.Status.OK, MIME, fis);
                        res.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                        return res;
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (session.getUri().equals("/Roboto-Regular.ttf")) {

                    try {
                        is = MainActivity.assetManager.open("Roboto-Regular.ttf");
                        return newFixedLengthResponse(Response.Status.OK, "application/x-font-ttf", is, is.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (session.getUri().equals("/upload")) {
                    String name = session.getHeaders().get("file-name");
                    FileOutputStream fos = null;
                    try {
                        File file = new File(dir, name);
                        file.createNewFile();
                        fos = new FileOutputStream(file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    InputStream in = session.getInputStream();
                    int count;
                    byte[] buffer = new byte[8192];
                    try {
                        while ((count = in.read(buffer)) > 0) {
                            try {
                                fos.write(buffer, 0, count);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "ok i am");
                } else if (session.getUri().equals("/")) {
                    try {
                        is = MainActivity.assetManager.open("indexTesting.html");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        return newFixedLengthResponse(Response.Status.OK, "text/html", is, is.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (session.getUri().equals("/script.js")) {
                    try {
                        is = MainActivity.assetManager.open("script.js");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        return newFixedLengthResponse(Response.Status.OK, "text/javascript", is, is.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT,"Admin has not allowed to view this page");

        }

    }//END OF MYHTTPSERVER

    class MyWebSocket extends NanoWSD.WebSocket {

        MyHttpServer httpServer;
        NanoHTTPD.IHTTPSession httpSession;
        String group;



        public MyWebSocket(NanoHTTPD.IHTTPSession handshakeRequest, MyHttpServer httpServer) {
            super(handshakeRequest);
            this.httpSession = handshakeRequest;
            this.httpServer = httpServer;
        }

        @Override
        protected void onOpen() {
        }

        @Override
        protected void onClose(NanoWSD.WebSocketFrame.CloseCode code, String reason, boolean initiatedByRemote) {
            this.httpServer.getConnections().get(group).remove(this);
            if(httpServer.getConnections().get(group).isEmpty()){
                httpServer.getConnections().remove(group);
            }
        }

        @Override
        protected void onMessage(NanoWSD.WebSocketFrame message) {

            try {
                JSONObject json = new JSONObject(message.getTextPayload());
                if (json.has("handShake")) {
                    String group = json.getString("handShake").trim();
                    this.group = group;
                    if (this.httpServer.getConnections().containsKey(group)) {
                        this.httpServer.getConnections().get(group).add(this);
                    } else {
                        ArrayList<MyWebSocket> list = new ArrayList<>();
                        list.add(this);
                        this.httpServer.getConnections().put(group, list);
                    }
                    boolean exists = fileExists(group);
                    if (exists) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(new File(dir, group + ".txt")));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sendMessage(line);
                            }
                            reader.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (json.has("message")) {
                    String msg = json.toString();
                    for (MyWebSocket ws : this.httpServer.getConnections().get(group)) {
                        ws.send(msg);
                    }
                    PrintWriter writer = new PrintWriter(new FileOutputStream(new File(dir, group + ".txt"), true), true);
                    writer.println(msg);
                    writer.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private boolean fileExists(String group){

            File file = new File(dir, group+".txt");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }else{
                return true;
            }

        }

        @Override
        protected void onPong(NanoWSD.WebSocketFrame pong) {

        }

        @Override
        protected void onException(IOException exception) {

        }

        public void sendMessage(String message){
            try {
                this.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//END OF MYWEBSOCKET


    class Reciever extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }



    }


}




