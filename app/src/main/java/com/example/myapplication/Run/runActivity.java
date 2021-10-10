package com.example.myapplication.Run;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Coaching;
import com.example.myapplication.MainAct;
import com.example.myapplication.Profile.User;
import com.example.myapplication.Profile.myfriendlist_Adapter;
import com.example.myapplication.R;
import com.example.myapplication.Request.RequestHttpURLConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class runActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    ImageView btn_run;
    ImageView btn_stop;
    ImageView btn_camera;
    TextView viewdistance;
    TextView viewpace;
    Boolean runstate;
    int time = 0;
    Boolean startset = true;
    private Location mCurrentLocation;
    Handler timehandler;
    Handler Kcalhandler;
    TextView view_time;
    Thread timethread;
    double latitude, longitude;
    private LatLng startLatLng ;        //polyline 시작점
    private LatLng endLatLng = new LatLng(0, 0);        //polyline 끝점
    String fileName;
    List<Polyline> polylines;
    private Marker mCurrentMarker;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    double lat;
    double lng;
    int wholedistance = 0;
    Boolean restart = false;
    Handler distancehandler;
    LocationCallback locationCallback;
    File file;
    ArrayList<String> arr_img;
    ArrayList<String> arr_storageimg;
    String mCurrentPhotoPath;
    ArrayList<String> arr_photopath;
    File photofile = null; // 사진 촬영 후 저장 할 파일
    private TextToSpeech tts;
    Intent serviceIntent;
    TextView viewkcal_runact;
    double kcal;
    double lastlat;
    double lastlag;
    JSONObject root;
    String uu;
    ArrayList<Double> arr_lat = new ArrayList<>();
    ArrayList<Double> arr_lng = new ArrayList<>();
    final int[] walk = {0};
    String coachcontent;
    FrameLayout runcoach;
    FrameLayout walkcoach;
    FrameLayout restcoach;
    JSONObject coachjson;
    ImageView btn_viewfriends;
    int prev_coachstate=999;
    int coachpass = 0;
    boolean polystate = true;
    boolean run = false;
    String addmsg;

    Button btn_msgsend;
    // myfriend rc
    ArrayList<User> myfrindInfoArrayList;
    myfriendlist_Adapter myfriend_adapter;
    RecyclerView myfrd_recyclerView;

    // runmsg rc
    ArrayList<Msg> arr_msg;
    RunMsgAdapter msgAdapter;
    RecyclerView rc_msg;

    SharedPreferences loginshared;
    String UserID;

    // 소켓 통신
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "3.143.9.214";
    private int port = 5001;

    TextView textView;
    Button connectbutton;
    Button chatbutton;
    TextView chatView;
    EditText message;
    String sendmsg;
    String read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runact);

        // 로그인 정보
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        UserID = loginshared.getString("id", null);

        mHandler = new Handler();


        Toast.makeText(runActivity.this,"러닝시작 합니다.",Toast.LENGTH_SHORT).show();
        serviceIntent = new Intent(runActivity.this, runService.class);
        startService(serviceIntent);

        btn_run = findViewById(R.id.btn_run);
        btn_stop = findViewById(R.id.btn_stop);
        view_time = findViewById(R.id.time);
        btn_viewfriends = findViewById(R.id.btn_viewfriends);

        MapView mapView = (MapView) findViewById(R.id.map);
        viewpace = findViewById(R.id.viewpace_runact);
        viewdistance = findViewById(R.id.distance_runActivity);
        viewkcal_runact = findViewById(R.id.viewkcal_runact);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        speech("러닝을 시작합니다");

        polylines = new ArrayList<>();
        runstate = true;
        btn_camera = findViewById(R.id.camera);

        arr_img = new ArrayList<String>();
        arr_storageimg = new ArrayList<>();
        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");
        arr_photopath = new ArrayList<String>();

        createLocationRequest();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // 메시지 전송 위한 소켓 생성
        new Thread() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sendWriter.println(UserID);
                    sendWriter.flush();

                    while(true){
                        read = input.readLine();

                        if(read!=null){
                            mHandler.post(new msgUpdate(read));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } }}.start();

        if(getIntent().getStringExtra("coachjson")!= null){
            coachcontent = getIntent().getStringExtra("coachjson");
            runcoach = findViewById(R.id.coach_run);
            walkcoach = findViewById(R.id.coach_walk);
            restcoach = findViewById(R.id.coach_rest);

            coachjson = null;
            try {
                coachjson = new JSONObject(coachcontent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("coachjson",coachjson+"");
        }


        Kcalhandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int time = bundle.getInt("time");
            }
        };

        timehandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int time = bundle.getInt("time");
                String totaltime = TimeToFormat(time);
                view_time.setText(totaltime);

                if(coachjson != null){
                    try {
                        JSONArray jsonObject1 = null;
                        jsonObject1 = coachjson.getJSONArray("rundata");

                        JSONObject data = null;
                        data = jsonObject1.getJSONObject(coachpass);
                        Log.e("data",data.toString());

                        Log.e("datatime",data.getInt("time")+"");
                        Log.e("time",time+"");
                        if(data.getInt("time") == time){
                            Log.e("behave",data.getInt("behave")+"");

                            switch(data.getInt("behave")){
                              case 0:       // 휴식
                                  if(prev_coachstate != data.getInt("behave")){
                                  restcoach.setVisibility(View.VISIBLE);
                                  walkcoach.setVisibility(View.INVISIBLE);
                                  runcoach.setVisibility(View.INVISIBLE);
                                      speech("잠시 휴식 하십시오.");
                                  }
                                  run= false;
                                  polystate = false;
                                break;
                              case 1:           // 걷기
                                  if(prev_coachstate != data.getInt("behave")) {
                                      walkcoach.setVisibility(View.VISIBLE);
                                      restcoach.setVisibility(View.INVISIBLE);
                                      runcoach.setVisibility(View.INVISIBLE);
                                      speech("걷기를 시작합니다.");
                                  }
                                  run= false;
                                  polystate = true;
                                 break;
                              case 2:           // 뛰기
                                  if(prev_coachstate != data.getInt("behave")) {
                                      runcoach.setVisibility(View.VISIBLE);
                                      restcoach.setVisibility(View.INVISIBLE);
                                      walkcoach.setVisibility(View.INVISIBLE);
                                      speech("빠른 달리기를 시작합니다.");

                                  }
                                  run = true;
                                  polystate = true;
                                 break;
                              default:
                                break;
                            }
                            prev_coachstate = data.getInt("behave");
                            Log.e("prev_coachstate",prev_coachstate+"");
                            coachpass++;
                        }


                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        time_start();

        mapView.getMapAsync(this);

        // 구글 apiclient 개체를 통해 구글 플레이 서비스 라이브러러에 제공된 구글 api에 액세스 할 수 있다.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(runActivity.this)
                    .addApi(LocationServices.API)   // 어떤 api 사용하는지
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        // 활동중인 친구보기

        btn_viewfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frdlistrequest(UserID);
            }
        });

        // 카메라 모양 클릭 버튼
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ContextCompat.checkSelfPermission(runActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permission2 = ContextCompat.checkSelfPermission(runActivity.this, Manifest.permission.CAMERA);
                // 권한이 열려있는지 확인
                if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
                    // 마쉬멜로우 이상버전부터 권한을 물어본다
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
                    } return;
                }else{
                    Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 임시로 사용할 파일
                    File tempDir = getCacheDir();
                    String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
                    String fileName = "running"+timeStamp;

                    File imageDir = null;
                    try {
                        imageDir = File.createTempFile(
                                fileName,       // 파일 이름
                                ".jpg",     // 파일 형식
                                tempDir
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mCurrentPhotoPath = imageDir.getAbsolutePath();
                    Log.e("photopath",mCurrentPhotoPath);
                    photofile = imageDir;

                    if(photofile != null){
                        // Uri 가져오기
                        Uri photoURI = FileProvider.getUriForFile(runActivity.this,"com.example.myapplication.fileprovider",photofile);
                        captureintent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                        startActivityForResult(captureintent, 101);
                    }
                }
            }
        });




        // 러닝 정지하고 완료하기 버튼
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(runActivity.this,RuncompleteActivity.class);
                intent.putStringArrayListExtra("arr_photopath",arr_photopath);
                intent.putStringArrayListExtra("arr_storageimg",arr_storageimg);
                intent.putExtra("distance",wholedistance);
                intent.putExtra("time",time);
                intent.putExtra("kcal",kcal);

                Toast.makeText(runActivity.this,"러닝종료 합니다.",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });


        btn_run.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (runstate){
                    btn_run.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    timethread.interrupt();
                    btn_stop.setVisibility(View.VISIBLE);
                    runstate = false;
                    polystate = false;
                    stopService(serviceIntent);
                    speech("러닝을 일시 중단합니다.");
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                } else {

                    btn_run.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    runstate = true;
                    restart = true;
                    polystate = true;
                    btn_stop.setVisibility(View.GONE);
                    enableMyLocation();

                    time_start();
                    startService(serviceIntent);
                    Toast.makeText(runActivity.this,"러닝을 다시 시작 합니다!",Toast.LENGTH_SHORT).show();
                    speech("러닝을 다시 시작합니다..");
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
       // mMap.setMyLocationEnabled(true);
        LatLng SEOUL = new LatLng(37.56, 126.97);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            String appKey = "l7xx5b0db2becaa848d483d7711ea0d3614c";
            String startX = "126.9726353116142";
            String startY = "37.55535952338017";
            String endX = "127.109999";
            String endY = "37.325168";
            String reqCoordType = "WGS84GEO";
            String resCoordType = "EPSG3857";
            String startName = URLEncoder.encode("서울역", "UTF-8");

            String endName = URLEncoder.encode("죽전역", "UTF-8");
            uu = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=result&appKey=" + appKey
                    + "&startX=" + startX + "&startY=" + startY + "&endX=" + endX + "&endY=" + endY
                    + "&startName=" + startName + "&endName=" + endName;
            url = new URL(uu);

        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //uri를 담아서 데이터를 보내면
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept-Charset", "utf-8");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //여기서 통신한 데이터의 결과값이 반환된다.
            System.out.println("뭘 가지고 오냐" + urlConn);
            NetworkTask networkTask = new NetworkTask(uu, null);
            networkTask.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void time_start() {
        timethread = new Thread(new Runnable() {
            @Override
            public void run(){
                while (runstate){
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = timehandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("time", time);
                    message.setData(bundle);
                    timehandler.sendMessage(message);
                }
            }
        });
        timethread.start();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
        //fusedLocationClient.removeLocationUpdates(locationCallback);

        // 소켓 끊기
        sendWriter.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGoogleApiClient.disconnect();
        timethread.interrupt();
        runstate = false;
        stopService(serviceIntent);
    }

    // polyline을 그려주는 메소드
    private void drawPath() {
        if(startLatLng == null) {
            startLatLng = new LatLng(lat, lng);
        }
        PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.GRAY).geodesic(true);
        polylines.add(mMap.addPolyline(options));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng,16));

    }


    // 거리 계산
    private double distanceTo(Location LatLng1,Location LatLng2){
        double distance = 0;
        distance = LatLng1.distanceTo(LatLng2);
        wholedistance += (int)distance;
        double kmdistance = (wholedistance / 1000.00);
        Log.e("kmdistance",""+kmdistance);
        viewdistance.setText(String.format("%.2f",kmdistance));

        setpace(kmdistance);
        return distance;
    }

    public void setpace(double distance){
        if(distance > 0){
            double t =((time/60.00)/60.00);
            double tt = distance / t;

            viewpace.setText(String.format("%.2f",tt));

            kcal += calKcal((float) tt,time,60)/time;

            viewkcal_runact.setText(String.format("%.2f",kcal));
        }
    }

    // googleapiclient 연결 상태가 변경 될때
    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location){
        double latitude = location.getLatitude(), longtitude = location.getLongitude();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private void enableMyLocation(){
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                locationCallback = new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                         super.onLocationResult(locationResult);
                        Location locationA = new Location("point A");
                        Location locationB = new Location("point B");

                       // lat = mCurrentLocation.getLatitude();
                        //lng = mCurrentLocation.getLongitude();

                        lat = arr_lng.get(walk[0]);
                        lng = arr_lat.get(walk[0]);
                        Log.e("lat",walk[0]+"");

                        if(startset){
                            setfirstmarker(lat,lng);
                        };
                        if (polystate) {
                            locationB.setLatitude(lat);
                            locationB.setLongitude(lng);

                            endLatLng = new LatLng(lat, lng);        //현재 위치를 끝점으로 설정
                            drawPath();                                            //polyline 그리기
                            locationA.setLatitude(startLatLng.latitude);
                            locationA.setLongitude(startLatLng.longitude);
                            startLatLng = new LatLng(lat, lng);        //시작점을 끝점으로 다시 설정

                            // 거리 측정
                            distanceTo(locationA,locationB);
                            walk[0]++;
                        }
                    }
                };

               fusedLocationClient.requestLocationUpdates( mLocationRequest, locationCallback,Looper.myLooper() );
            }
        }
    }

        protected void createLocationRequest(){
        if(startset){
            Log.e("startset?",startset+"1");
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000);    // 위치가 update되는 주기
            mLocationRequest.setFastestInterval(2000);  // 위치 획득 후 update되는 주기
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
            Log.e("startset?",startset+"2");

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(8000);    // 위치가 update되는 주기
            mLocationRequest.setFastestInterval(8000);  // 위치 획득 후 update되는 주기
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        PRIORITY_HIGH_ACCURACY : 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
//        PRIORITY_LOW_POWER : 저전력을 고려하며 정확도가 떨어짐
//        PRIORITY_NO_POWER : 추가적인 배터리 소모없이 위치정보 획득
//        PRIORITY_BALANCED_POWER_ACCURACY : 전력과 정확도의 밸런스를 고려. 정확도 다소 높음setSmallestDisplacement : 최소 거리 이동시 갱신 가능.
        }

        public void setfirstmarker(double latitude, double longtitude){
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(latitude , longtitude));

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
            startset = false;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000) {

            Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 임시로 사용할 파일
            File tempDir = getCacheDir();
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String fileName = "running"+timeStamp;

            File imageDir = null;
            try {
                imageDir = File.createTempFile(
                        fileName,       // 파일 이름
                        ".jpg",     // 파일 형식
                        tempDir
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            mCurrentPhotoPath = imageDir.getAbsolutePath();
            Log.e("photopath",mCurrentPhotoPath);
            photofile = imageDir;

            if(photofile != null){
                // Uri 가져오기
                Uri photoURI = FileProvider.getUriForFile(runActivity.this,"com.example.myapplication.fileprovider",photofile);
                captureintent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(captureintent, 101);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK) {
            Uri test = Uri.fromFile(new File(mCurrentPhotoPath));
           saveFile(test);
        }
    }


    public static String TimeToFormat(int time){
        String totaltime = null;
        int sec = time % 60;
        if (time >= 3600) {
            int bun = time / 60 % 60;
            int hour = time / 3600;
            totaltime = String.format("%02d", hour) + ":" + String.format("%02d", bun) + ":" + String.format("%02d", sec);
        } else {
            int bun = time / 60 % 60;
            totaltime = String.format("%02d", bun) + ":" + String.format("%02d", sec);
        }
        return totaltime;
    }

    // 파일 저장
    private void saveFile(Uri image_uri) {
        ContentValues values = new ContentValues();
         fileName =  "Run"+System.currentTimeMillis()+".png";
        values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        arr_photopath.add(String.valueOf(item));
        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
            if (pdf == null) {
                Log.d("Run", "null");
            } else {
                byte[] inputData = getBytes(image_uri);
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(inputData);
                fos.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    contentResolver.update(item, values, null, null);
                }
                // 갱신
                galleryAddPic(fileName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Run", "FileNotFoundException  : "+e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d("Run", "FileOutputStream = : " + e.getMessage());
        }
    }

    private void galleryAddPic(String Image_Path) {
        String SaveFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Image_Path;
        Log.e("galleryaddpic_Image_Path",Image_Path);
        arr_storageimg.add(SaveFolderPath);

        File file = new File(Image_Path);
        MediaScannerConnection.scanFile(runActivity.this,
                new String[]{file.toString()},
                null, null);

        Log.e("save",file.toString());
    }

    public byte[] getBytes(Uri image_uri) throws IOException {
        InputStream iStream = getContentResolver().openInputStream(image_uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024; // 버퍼 크기
        byte[] buffer = new byte[bufferSize]; // 버퍼 배열

        int len = 0;
        // InputStream에서 읽어올 게 없을 때까지 바이트 배열에 쓴다.
        while ((len = iStream.read(buffer)) != -1)
            byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }

    public void speech(String txt){
        tts = new TextToSpeech(runActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
                tts.speak(txt,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
    }

    public double calKcal(float pace,int time, int weight){
         double met;
         Log.e("pace, time",pace+" : "+time+"");
         if( pace <=4 && pace >= 3){
             met = 3;

         }else if( pace <=5 && pace >= 4 ){
             met = 3.5;

         }else if( pace <= 6.4 && pace >= 5){
            met = 4;

         }else if(pace <= 7 && pace >= 6.5){
            met = 5;

         }else if(pace <= 7 && pace <= 8){
            met = 7;

         }else if(pace <= 16 && pace >= 8){
            met = 10;
         }else if(pace >= 16){
            met = 16;
         }else{
             met = 1;
         }
         Log.e("met",met+"");
        Log.e("time",time+"");
        double kcal = ((3.5 * met  * weight * (time / 60.0))/1000.0) * 5;

        return  kcal;
    }


        public class NetworkTask extends AsyncTask<Void, Void, String> {
            private String url;
            private ContentValues values;
            public NetworkTask(String url, ContentValues values) {
                this.url = url;
                this.values = values;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String result;
                // 요청 결과를 저장할 변수.
                RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
                result = requestHttpURLConnection.request(url, values);
                // 해당 URL로 부터 결과물을 얻어온다.
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
                try {
                    //전체 데이터를 제이슨 객체로 변환
                     root = new JSONObject(s);
                    System.out.println("제일 상위 "+root);
                    int length =root.getJSONArray("features").length()-1;
                    Log.e("length",length+"");
                                for(int i = 0; i<=length;i++){
                                    //전체 데이터중에 features리스트의 첫번째 객체를 가지고 오기
                                    JSONObject features = null;
                                    try {
                                      //  System.out.println(i);
                                        features = (JSONObject) root.getJSONArray("features").get(i);

                                   // System.out.println("상위에서 "+i+" 리스트 "+features);

                                    //리스트의 첫번째 객체에 있는 geometry가져오기
                                    JSONObject geometry =  features.getJSONObject("geometry");
                                    //System.out.println("리스트에서 geometry 객체 "+geometry);
                                    if(geometry.getString("type").equals("LineString")){
                                        for(int j = 0; j<=geometry.getJSONArray("coordinates").length()-1;j++){
                                            //최종적으로 위도와 경도를 가져온다.
                                            String loc = geometry.getJSONArray("coordinates").get(j).toString();
                                           // Log.e("loc"+j,loc+"");
                                            String[] latlong =  loc.split(",");
                                            int idx = latlong[0].indexOf("[");
                                            int idx2 = latlong[1].indexOf("]");
                                            latlong[0] = latlong[0].substring(idx+1);
                                           // Log.e("latlong0",latlong[0]);
                                            double lat = Double.parseDouble(latlong[0]);
                                            //Log.e("double",lat+"");
                                            latlong[1] = latlong[1].substring(0,idx2);
                                           // Log.e("latlong1",latlong[1]);
                                            double lag = Double.parseDouble(latlong[1]);
                                          //  chglocation(Double.parseDouble(latlong[0]),Double.parseDouble(latlong[1]));
                                            arr_lat.add(lat);
                                            arr_lng.add(lag);
                                            Log.e("lat",lat+"");
                                        }
                                        }
                                        }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                    mGoogleApiClient.connect();
                    enableMyLocation();

//                textView.setText((CharSequence) root);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void chglocation(double lat,double lag){
            Log.e("lat",lat+"");
            Log.e("lag",lag+"");

            Location locationA = new Location("point A");
            Location locationB = new Location("point B");

            if(startset){
                setfirstmarker(lat,lag);
            };

            if (polystate) {
                locationB.setLatitude(lat);
                locationB.setLongitude(lag);

                endLatLng = new LatLng(lat, lag);        //현재 위치를 끝점으로 설정
                drawPath();                                            //polyline 그리기
                locationA.setLatitude(startLatLng.latitude);
                locationA.setLongitude(startLatLng.longitude);
                startLatLng = new LatLng(lat, lag);        //시작점을 끝점으로 다시 설정

                // 거리 측정
                distanceTo(locationA,locationB);
            }
        }


        // 친구에게 메시지전송 메소드
    public class viewfriendsdialog {
        Dialog dig;
        Button btn_setdistance;
        ImageView
                btn_addmsg;

        public void calldialog() {

            dig = new Dialog(runActivity.this);
            // 액티비티의 타이틀바를 숨긴다.
            dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dig.setContentView(R.layout.dialog_friends);
            myfrd_recyclerView = dig.findViewById(R.id.rc_friends);

            btn_msgsend = (Button) dig.findViewById(R.id.btn_msgsend);

            // 라사이클러뷰에 넣기
            myfriend_adapter = new myfriendlist_Adapter(UserID,myfrindInfoArrayList,2);

            LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(runActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            myfrd_recyclerView.setLayoutManager(linearLayoutManager);
            myfrd_recyclerView.setAdapter(myfriend_adapter);

            // 라사이클러뷰에 넣기
            rc_msg = dig.findViewById(R.id.rc_message);
            btn_addmsg = dig.findViewById(R.id.btn_addmsg);
            arr_msg = new ArrayList<>();
            Msg msg = new Msg();
            msg.setMsg("안녕하세요1");
            arr_msg.add(msg);
            Msg msg1 = new Msg();
            msg1.setMsg("안녕하세요2");
            arr_msg.add(msg1);

            msgAdapter = new RunMsgAdapter(arr_msg);

            LinearLayoutManager linearLayoutManager2 =  new LinearLayoutManager(runActivity.this);
            linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
            rc_msg.setLayoutManager(linearLayoutManager2);
            rc_msg.setAdapter(msgAdapter);

            btn_addmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new inputmsgdialog().calldialog();

                }
            });

            btn_msgsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ToUserId;
                    ToUserId = myfrindInfoArrayList.get(myfriend_adapter.getselectpos()).getId();

                    sendmsg = arr_msg.get(msgAdapter.getselpos()).getMsg();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sendWriter.println(UserID +"@"+ToUserId+"@"+sendmsg);
                                sendWriter.flush();
                                message.setText("");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
            dig.show();
        }
    }


    public void frdlistrequest(String mid){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/myfriendinfo.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",String.valueOf(jsonObject));
                    int fnum = jsonObject.getInt("fnum");
                    if(fnum>0){
                        myfrindInfoArrayList = new ArrayList<>();
                        for(int i=0;i<fnum;i++){
                            User user = new User();
                            user.setId(jsonObject.getString("friend"+i));
                            myfrindInfoArrayList.add(user);
                        }
                    }

                    viewfriendsdialog viewfriendsdialog = new viewfriendsdialog();
                    viewfriendsdialog.calldialog();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("f_id", mid);

        // 서버에 데이터 보내고 응답 요청
//              RequestQueue requestQueue = Volley.newRequestQueue(context);
//              requestQueue.add(smpr);
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(runActivity.this);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }
    }

    // 메시지 받고 뷰 업데이트 메소드
    class msgUpdate implements Runnable{
        private String msg;
        AlertDialog.Builder builder;

        public msgUpdate(String str) {this.msg=str;
        }

        @Override
        public void run() {
            builder = new AlertDialog.Builder(runActivity.this);
            builder.setTitle("메시지")        // 제목 설정
                    .setMessage(msg)        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        }
    }

     public class inputmsgdialog {
            Dialog dig;
            EditText editmsg;
            Button btn_msgsend;

            public void calldialog() {
                dig = new Dialog(runActivity.this);
                // 액티비티의 타이틀바를 숨긴다.
                dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // 커스텀 다이얼로그의 레이아웃을 설정한다.
                dig.setContentView(R.layout.dialog_inputmsg);
                editmsg = dig.findViewById(R.id.editmsg);
                btn_msgsend = dig.findViewById(R.id.btn_msgsend);

                btn_msgsend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addmsg = editmsg.getText().toString();
                        msgAdapter.addmsg(addmsg);
                        dig.dismiss();
                    }
                });

                dig.show();
            }
        }

}




