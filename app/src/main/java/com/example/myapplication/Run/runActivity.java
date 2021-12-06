package com.example.myapplication.Run;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.myapplication.MySingleton;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.io.OutputStream;
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
    Handler setmsghandler;

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
    String chkonlinemsg;

    ArrayList<friendmsg> friendmsgArrayList;

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


    //dialog
    Dialog ViewFriendDialog;

    // 소켓 통신
    private Handler mHandler;
    private Handler mHandler2;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "3.12.49.32";
    private int port = 5001;
    TextView txt_nomsg;
    TextView msg_num;
    TextView textView;
    Button connectbutton;
    Button chatbutton;
    TextView chatView;
    EditText message;
    String sendmsg;
    String read;
    OutputStream socketoutputStream;

    MarkerOptions startmakerOptions;
    Bundle savedInstanceState;
    Button btn_camera2;
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.runact);

        Log.e("runactivity","log1");

        // 로그인 정보
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        UserID = loginshared.getString("id", null);
        Log.e("runactivity","log2");
        mHandler = new Handler();
        mHandler2 = new Handler();
        Log.e("runactivity","log3");
        Toast.makeText(runActivity.this,"러닝시작 합니다.",Toast.LENGTH_SHORT).show();
        serviceIntent = new Intent(runActivity.this, runService.class);
        startService(serviceIntent);
        Log.e("runactivity","log4");

        friendmsgArrayList = new ArrayList<>();
        Log.e("runactivity","log5");

        btn_camera2 = findViewById(R.id.button23);

        btn_run = findViewById(R.id.btn_run);
        btn_stop = findViewById(R.id.btn_stop);
        view_time = findViewById(R.id.time);
        btn_viewfriends = findViewById(R.id.btn_viewfriends);
        Log.e("runactivity","log6");
         mapView = (MapView) findViewById(R.id.map);
        viewpace = findViewById(R.id.viewpace_runact);
        viewdistance = findViewById(R.id.distance_runActivity);
        viewkcal_runact = findViewById(R.id.viewkcal_runact);
        msg_num = findViewById(R.id.msg_num);
        Log.e("runactivity","log7");
        mapView.onCreate(savedInstanceState);
        Log.e("runactivity","log8");
        mapView.onResume();
        mapView.getMapAsync(this);
        Log.e("runactivity","log9");
        speech("러닝을 시작합니다");
        Log.e("runactivity","log10");
        polylines = new ArrayList<>();
        runstate = true;
        btn_camera = findViewById(R.id.camera);
        Log.e("runactivity","log11");
        arr_img = new ArrayList<String>();
        arr_storageimg = new ArrayList<>();
        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");
        arr_photopath = new ArrayList<String>();
        Log.e("runactivity","log12");
        createLocationRequest();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Log.e("runactivity","log13");
        // 메시지 초기화
        arr_msg = new ArrayList<>();

        // 메시지 개수 보여주는 숫자 클릭이벤트
        msg_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new msgboxdialog().calldialog();
            }
        });
        Log.e("runactivity","log14");
        // 메시지 전송 위한 소켓 생성
        /*
        new Thread() {
            public void run() {
                try {
                    Log.e("socketthread",Thread.currentThread().getName());

                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    socketoutputStream = socket.getOutputStream();
                    sendWriter = new PrintWriter(socketoutputStream);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sendWriter.println(UserID);
                    sendWriter.flush();

                    String[] splited;

                    while(true){
                        read = input.readLine();
                        if(read!=null){
                            splited = read.split("@");
                            Log.e("tcpread",read);
                            if(splited[0].equals("chkonline")){
                                    Log.e("chk","chkonline2");
                                    mHandler.post(new setonline2(splited));
                            }else if(splited[0].equals("msg")){
                                // 친구 메시지 객체 생성
                                friendmsg friendmsg = new friendmsg();

                                // 상대방이 만약 위치값을 보냈다면
                                String friendlocation;
                                if(Boolean.parseBoolean(splited[4])){
                                    Log.e("splited[5]",splited[5]);
                                    // 구글 맵에 표시할 마커에 대한 옵션 설정
                                    mHandler2.post(new friendsloactionupdate(splited));
                                    LatLng latLng = new LatLng(Double.parseDouble(splited[5]), Double.parseDouble(splited[6]));

                                    friendmsg.setLoaction(latLng);
                                }

                                mHandler.post(new msgUpdate(splited[1],splited[3]));

                                friendmsg.setId(splited[1]);
                                friendmsg.setContent(splited[3]);
                                friendmsgArrayList.add(friendmsg);

                                Message message = setmsghandler.obtainMessage();
                                Bundle bundle = new Bundle();
                                bundle.putInt("size",friendmsgArrayList.size());
                                message.setData(bundle);
                                setmsghandler.sendMessage(message);
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } }}.start();
*/
        Log.e("runactivity","log15");
        if(getIntent().getStringExtra("coachjson")!= null){
            coachcontent = getIntent().getStringExtra("coachjson");
            runcoach = findViewById(R.id.coach_run);
            walkcoach = findViewById(R.id.coach_walk);
            restcoach = findViewById(R.id.coach_rest);
            Log.e("runactivity","log16");
            coachjson = null;
            try {
                coachjson = new JSONObject(coachcontent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("runactivity","log17");
            Log.e("coachjson",coachjson+"");
        }

        Log.e("runactivity","log18");
        Kcalhandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int time = bundle.getInt("time");
            }
        };
        Log.e("runactivity","log19");
        setmsghandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.e("runactivity","log20");
                super.handleMessage(msg);
                Log.e("runactivity","log21");
                Bundle bundle = msg.getData();
                int size = bundle.getInt("size");
                Log.e("runactivity","log22");
                msg_num.setText(size+"");
            }
        };
        Log.e("runactivity","log23");
        timehandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e("runactivity","log24");
                Bundle bundle = msg.getData();
                Log.e("runactivity","log25");
                int time = bundle.getInt("time");
                Log.e("runactivity","log26");
                String totaltime = TimeToFormat(time);
                Log.e("runactivity","log27");
                view_time.setText(totaltime);
                Log.e("runactivity","log28");
                if(coachjson != null){
                    try {
                        Log.e("runactivity","log29");
                        JSONArray jsonObject1 = null;
                        jsonObject1 = coachjson.getJSONArray("rundata");
                        Log.e("runactivity","log30");
                        JSONObject data = null;
                        data = jsonObject1.getJSONObject(coachpass);
                        Log.e("data",data.toString());
                        Log.e("runactivity","log31");
                        Log.e("datatime",data.getInt("time")+"");
                        Log.e("time",time+"");
                        if(data.getInt("time") == time){
                            Log.e("behave",data.getInt("behave")+"");
                            Log.e("runactivity","log31");
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
                            Log.e("runactivity","log32");
                            prev_coachstate = data.getInt("behave");
                            Log.e("runactivity","log33");
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
        Log.e("runactivity","log34");
        // 구글 apiclient 개체를 통해 구글 플레이 서비스 라이브러러에 제공된 구글 api에 액세스 할 수 있다.
        if (mGoogleApiClient == null) {
            Log.e("runactivity","log35");
            mGoogleApiClient = new GoogleApiClient.Builder(runActivity.this)
                    .addApi(LocationServices.API)   // 어떤 api 사용하는지
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            Log.e("runactivity","log36");
        }

        // 활동중인 친구보기
        btn_viewfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("runactivity","log37");
                frdlistrequest(UserID);
                Log.e("runactivity","log38");

            }
        });
        btn_camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(runActivity.this,runActivity.class);
                intent.putExtra("coachjson",getIntent().getStringExtra("coachjson"));
                startActivity(intent);
                finish();
            }
        });

        // 카메라 모양 클릭 버튼

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("runactivity","log39");
                int permission = ContextCompat.checkSelfPermission(runActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permission2 = ContextCompat.checkSelfPermission(runActivity.this, Manifest.permission.CAMERA);
                // 권한이 열려있는지 확인
                if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
                    Log.e("runactivity","log40");
                    // 마쉬멜로우 이상버전부터 권한을 물어본다
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
                        Log.e("runactivity","log41");
                    } return;
                }else{
                    Log.e("runactivity","log42");
                    Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 임시로 사용할 파일
                    Log.e("runactivity","log43");
                    File tempDir = getCacheDir();
                    Log.e("runactivity","log44");
                    String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
                    String fileName = "running"+timeStamp;
                    Log.e("runactivity","log45");
                    File imageDir = null;
                    Log.e("runactivity","log46");
                    try {
                        imageDir = File.createTempFile(
                                fileName,       // 파일 이름
                                ".jpg",     // 파일 형식
                                tempDir
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("runactivity","log47");
                    mCurrentPhotoPath = imageDir.getAbsolutePath();
                    Log.e("runactivity","log48");
                    Log.e("photopath",mCurrentPhotoPath);
                    photofile = imageDir;
                    Log.e("runactivity","log49");
                    if(photofile != null){
                        // Uri 가져오기
                        Uri photoURI = FileProvider.getUriForFile(runActivity.this,"com.example.myapplication.fileprovider",photofile);
                        Log.e("runactivity","log50");
                        captureintent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                        startActivityForResult(captureintent, 101);
                    }
                }
            }
        });

        Log.e("runactivity","log51");


        // 러닝 정지하고 완료하기 버튼
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("runactivity","log52");
                Intent intent = new Intent(runActivity.this,RuncompleteActivity.class);
                Log.e("runactivity","log53");
                intent.putStringArrayListExtra("arr_photopath",arr_photopath);
                intent.putStringArrayListExtra("arr_storageimg",arr_storageimg);
                intent.putExtra("distance",wholedistance);
                intent.putExtra("time",time);
                intent.putExtra("kcal",kcal);
                Log.e("runactivity","log54");
                Toast.makeText(runActivity.this,"러닝종료 합니다.",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                Log.e("runactivity","log55");
                finish();
                Log.e("runactivity","log56");
            }
        });

        Log.e("runactivity","log57");
        btn_run.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e("runactivity","log58");
                if (runstate){
                    Log.e("runactivity","log59");
                    btn_run.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    Log.e("runactivity","log60");
                    timethread.interrupt();
                    Log.e("runactivity","log61");
                    btn_stop.setVisibility(View.VISIBLE);
                    Log.e("runactivity","log62");
                    runstate = false;
                    polystate = false;
                    Log.e("runactivity","log63");
                    stopService(serviceIntent);
                    speech("러닝을 일시 중단합니다.");
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    Log.e("runactivity","log64");
                }else{
                    btn_run.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    runstate = true;
                    restart = true;
                    polystate = true;
                    btn_stop.setVisibility(View.GONE);
                    Log.e("runactivity","log65");
                    enableMyLocation();
                    Log.e("runactivity","log66");
                    time_start();
                    Log.e("runactivity","log67");
                    startService(serviceIntent);
                    Log.e("runactivity","log68");
                    Toast.makeText(runActivity.this,"러닝을 다시 시작 합니다!",Toast.LENGTH_SHORT).show();
                    speech("러닝을 다시 시작합니다..");
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        Log.e("runactivity","log69");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.e("runactivity","log70");
       // mMap.setMyLocationEnabled(true);

        LatLng SEOUL = new LatLng(37.56, 126.97);
        Log.e("runactivity","log71");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        Log.e("runactivity","log72");
        URL url = null;

        HttpURLConnection urlConn = null;
        try {
            String appKey = "l7xx5b0db2becaa848d483d7711ea0d3614c";
            String startX = "127.096836";
            String startY = "37.321257";
            String endX = "127.109999";
            String endY = "37.325168";
            String reqCoordType = "WGS84GEO";
            String resCoordType = "EPSG3857";
            String startName = URLEncoder.encode("판교역", "UTF-8");
            Log.e("runactivity","log73");
            String endName = URLEncoder.encode("죽전역", "UTF-8");
            uu = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=result&appKey=" + appKey
                    + "&startX=" + startX + "&startY=" + startY + "&endX=" + endX + "&endY=" + endY
                    + "&startName=" + startName + "&endName=" + endName;
            url = new URL(uu);
            Log.e("runactivity","log74");
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //uri를 담아서 데이터를 보내면
            Log.e("runactivity","log75");
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept-Charset", "utf-8");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.e("runactivity","log76");
            //여기서 통신한 데이터의 결과값이 반환된다.
            System.out.println("뭘 가지고 오냐" + urlConn);
            NetworkTask networkTask = new NetworkTask(uu, null);
            networkTask.execute();
            Log.e("runactivity","log77");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("runactivity","log78");
        mMap.setOnMarkerClickListener(markerClickListener);
        Log.e("runactivity","log79");
    }

    //마커 클릭 리스너

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(marker.getTitle() != null){
                Log.e("runactivity","log81");
                // 현재위치와 메시지를 보낸 곳의 위치
                Location location1 = new Location("point A");
                location1.setLatitude(lat);
                location1.setLongitude(lng);
                Log.e("runactivity","log82");
                Location location2 = new Location("point B");
                location2.setLatitude(marker.getPosition().latitude);
                location2.setLongitude(marker.getPosition().longitude);
                Log.e("runactivity","log83");
                double kmdistance = ((location1.distanceTo(location2)) / 1000.00);
                Log.e("runactivity","log84");
                AlertDialog.Builder builder = new AlertDialog.Builder(runActivity.this);
                                    builder.setTitle(marker.getTitle())        // 제목 설정
                                            .setMessage("내용 : "+marker.getSnippet()+"\n(나와의 거리 : " + String.format("%.2f",kmdistance) +"킬로미터)")        // 메세지 설정
                                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                                public void onClick(DialogInterface dialog, int whichButton){
                                                  dialog.dismiss();
                                                }
                                            });
                Log.e("runactivity","log85");
                                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                                    dialog.show();    // 알림창 띄우기
            }
            return true;
        }
    };



    public void time_start() {
        Log.e("runactivity","log86");
        timethread = new Thread(new Runnable() {
            @Override
            public void run(){
                Log.e("runactivity","log87");
                while (runstate){
                    Log.e("time_start","log1");
                    time++;
                    Log.e("time_start","log2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("timethreadname",Thread.currentThread().getName());
                    Message message = timehandler.obtainMessage();
                    Log.e("time_start","log3");
                    Bundle bundle = new Bundle();
                    bundle.putInt("time", time);
                    message.setData(bundle);
                    Log.e("time_start","log4");
                    timehandler.sendMessage(message);
                    Log.e("time_start","log5");
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

        Log.e("onstop","onstop");
        mGoogleApiClient.disconnect();
        //fusedLocationClient.removeLocationUpdates(locationCallback);
/*
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sendWriter.println("close@"+UserID);
                    sendWriter.flush();
                    Log.e("closewrite",Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
*/
        /*
        try {
            if(socketoutputStream != null){
                socketoutputStream.close();
                // 소켓 끊기
                sendWriter.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        fusedLocationClient.removeLocationUpdates(locationCallback);
        if(mapView != null){
            mapView.getOverlay().clear();
            mapView.removeAllViews();
            mapView.onDestroy();
        }


        for(Polyline line : polylines)
        {
            line.remove();
        }
        polylines.clear();

        mGoogleApiClient.disconnect();
        timethread.interrupt();
        runstate = false;

        stopService(serviceIntent);

    }

    // polyline을 그려주는 메소드
    private void drawPath() {
        Log.e("runactivity","log87");
        if(startLatLng == null) {
            startLatLng = new LatLng(lat, lng);
        }

        Log.e("runactivity","log88");
        PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.GRAY).geodesic(true);
        Log.e("runactivity","log89");
        polylines.add(mMap.addPolyline(options));
        Log.e("runactivity","log90");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng,16));
        Log.e("runactivity","log91");
    }


    // 거리 계산
    private double distanceTo(Location LatLng1,Location LatLng2){
        double distance = 0;
        Log.e("runactivity","log92");
        distance = LatLng1.distanceTo(LatLng2);
        Log.e("distanceto",distance+"");
        wholedistance += (int)distance;
        Log.e("runactivity","log93");
        double kmdistance = (wholedistance / 1000.00);
        Log.e("kmdistance",""+kmdistance);
        viewdistance.setText(String.format("%.2f",kmdistance));
        Log.e("runactivity","log94");
        setpace(kmdistance);
        Log.e("runactivity","log95");
        return distance;
    }

    public void setpace(double distance){
        if(distance > 0){
            Log.e("runactivity","log96");
            double t =((time/60.00)/60.00);
            double tt = distance / t;
            Log.e("time2",time+"");
            Log.e("runactivity","log97");
            int mdistance = (int)(distance * 1000.0);
            Log.e("distance2",mdistance+"");
            Log.e("runactivity","log98");
            double totalpace =((time*1000)/mdistance);
            int bunpace = (int)(totalpace/60);
            int secpace = (int)(totalpace % 60);
            Log.e("runactivity","log99");
            viewpace.setText(bunpace+"\'"+secpace+"\"");
            Log.e("runactivity","log100");
            kcal += calKcal((float) tt,time,60)/time;
            Log.e("runactivity","log101");
            viewkcal_runact.setText(String.format("%.2f",kcal));
        }
    }

    // googleapiclient 연결 상태가 변경 될때
    @Override
    public void onConnected(Bundle bundle) {
        Log.e("runactivity","log102");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("runactivity","log103");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("runactivity","log104");
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
        Log.e("runactivity","log105");
        if (mMap != null) {
            Log.e("runactivity","log106");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{
                Log.e("runactivity","log107");
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                Log.e("runactivity","log108");
                locationCallback = new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                         super.onLocationResult(locationResult);
                        Log.e("runactivity","log109");
                        Location locationA = new Location("point A");
                        Location locationB = new Location("point B");
                        Log.e("runactivity","log110");
                       // lat = mCurrentLocation.getLatitude();
                        //lng = mCurrentLocation.getLongitude();
                        Log.e("enableMyLocation",Thread.currentThread().getName());

                        lat = arr_lng.get(walk[0]);
                        lng = arr_lat.get(walk[0]);
                        Log.e("lat",walk[0]+"");
                        Log.e("runactivity","log111");
                        if(startset){
                            Log.e("runactivity","log112");
                            setfirstmarker(lat,lng);
                            Log.e("runactivity","log113");
                        };
                        Log.e("runactivity","log114");
                        if (polystate) {
                            Log.e("runactivity","log115");
                            locationB.setLatitude(lat);
                            locationB.setLongitude(lng);
                            Log.e("runactivity","log116");
                            endLatLng = new LatLng(lat, lng);        //현재 위치를 끝점으로 설정
                            Log.e("runactivity","log117");
                            drawPath();
                            Log.e("runactivity","log118");
                            //polyline 그리기
                            locationA.setLatitude(startLatLng.latitude);
                            locationA.setLongitude(startLatLng.longitude);
                            startLatLng = new LatLng(lat, lng);        //시작점을 끝점으로 다시 설정
                            Log.e("runactivity","log119");
                            // 거리 측정

                            distanceTo(locationA,locationB);
                            Log.e("runactivity","log120");
                            walk[0]++;
                        }
                    }
                };
                Log.e("runactivity","log121");
               fusedLocationClient.requestLocationUpdates( mLocationRequest, locationCallback,Looper.myLooper() );
                Log.e("runactivity","log122");


            }
        }
    }

        protected void createLocationRequest(){
            Log.e("runactivity","log123");
        if(startset){
            Log.e("runactivity","log124");
            Log.e("startset?",startset+"1");
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000);    // 위치가 update되는 주기
            mLocationRequest.setFastestInterval(2000);  // 위치 획득 후 update되는 주기
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

            Log.e("runactivity","log125");
            Log.e("startset?",startset+"2");

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(15000);    // 위치가 update되는 주기
            mLocationRequest.setFastestInterval(15000);  // 위치 획득 후 update되는 주기
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.e("runactivity","log126");
//        PRIORITY_HIGH_ACCURACY : 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
//        PRIORITY_LOW_POWER : 저전력을 고려하며 정확도가 떨어짐
//        PRIORITY_NO_POWER : 추가적인 배터리 소모없이 위치정보 획득
//        PRIORITY_BALANCED_POWER_ACCURACY : 전력과 정확도의 밸런스를 고려. 정확도 다소 높음setSmallestDisplacement : 최소 거리 이동시 갱신 가능.
        }

        public void setfirstmarker(double latitude, double longtitude){
            Log.e("runactivity","log127");
           startmakerOptions = new MarkerOptions();
            startmakerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(latitude , longtitude));
            Log.e("runactivity","log128");
            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(startmakerOptions);
            Log.e("runactivity","log129");
            startset = false;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("runactivity","log130");
        if(requestCode == 1000) {
            Log.e("runactivity","log131");
            Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 임시로 사용할 파일
            File tempDir = getCacheDir();
            Log.e("runactivity","log132");
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String fileName = "running"+timeStamp;
            Log.e("runactivity","log133");
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
            Log.e("runactivity","log134");
            mCurrentPhotoPath = imageDir.getAbsolutePath();
            Log.e("photopath",mCurrentPhotoPath);
            photofile = imageDir;
            Log.e("runactivity","log135");
            if(photofile != null){
                // Uri 가져오기
                Uri photoURI = FileProvider.getUriForFile(runActivity.this,"com.example.myapplication.fileprovider",photofile);
                captureintent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(captureintent, 101);
                Log.e("runactivity","log136");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("runactivity","log137");
        if(requestCode == 101 && resultCode == RESULT_OK) {
            Uri test = Uri.fromFile(new File(mCurrentPhotoPath));
           saveFile(test);
        }
        Log.e("runactivity","log138");

    }


    public static String TimeToFormat(int time){
        Log.e("runactivity","log139");
        String totaltime = null;
        int sec = time % 60;
        Log.e("runactivity","log140");
        if (time >= 3600) {
            Log.e("runactivity","log141");
            int bun = time / 60 % 60;
            int hour = time / 3600;
            totaltime = String.format("%02d", hour) + ":" + String.format("%02d", bun) + ":" + String.format("%02d", sec);
        } else {
            Log.e("runactivity","log142");
            int bun = time / 60 % 60;
            totaltime = String.format("%02d", bun) + ":" + String.format("%02d", sec);
        }
        Log.e("runactivity","log143");
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
                Log.e("NetworkTask","1");
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
                Log.e("NetworkTask","2");
                // 요청 결과를 저장할 변수.
                RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
                result = requestHttpURLConnection.request(url, values);
                // 해당 URL로 부터 결과물을 얻어온다.
                Log.e("NetworkTask","3");
                return result;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("NetworkTask","4");
                //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
                try {
                    Log.e("NetworkTask","5");
                    //전체 데이터를 제이슨 객체로 변환
                     root = new JSONObject(s);
                    Log.e("NetworkTask","6");
                    System.out.println("제일 상위 "+root);
                    Log.e("NetworkTask","7");
                    int length =root.getJSONArray("features").length()-1;
                    Log.e("length",length+"");

                                for(int i = 0; i<=length;i++){
                                    //전체 데이터중에 features리스트의 첫번째 객체를 가지고 오기
                                    JSONObject features = null;
                                    try {
                                      //  System.out.println(i);
                                        features = (JSONObject) root.getJSONArray("features").get(i);
                                        Log.e("NetworkTask","8");
                                   // System.out.println("상위에서 "+i+" 리스트 "+features);

                                    //리스트의 첫번째 객체에 있는 geometry가져오기
                                    JSONObject geometry =  features.getJSONObject("geometry");
                                        Log.e("NetworkTask","9");
                                    //System.out.println("리스트에서 geometry 객체 "+geometry);
                                    if(geometry.getString("type").equals("LineString")){
                                        Log.e("NetworkTask","10");
                                        for(int j = 0; j<=geometry.getJSONArray("coordinates").length()-1;j++){
                                            //최종적으로 위도와 경도를 가져온다.
                                            Log.e("NetworkTask","11");
                                            String loc = geometry.getJSONArray("coordinates").get(j).toString();
                                           // Log.e("loc"+j,loc+"");
                                            Log.e("NetworkTask","12");
                                            String[] latlong =  loc.split(",");
                                            Log.e("NetworkTask","13");
                                            int idx = latlong[0].indexOf("[");
                                            int idx2 = latlong[1].indexOf("]");
                                            Log.e("NetworkTask","14");
                                            latlong[0] = latlong[0].substring(idx+1);
                                           // Log.e("latlong0",latlong[0]);
                                            double lat = Double.parseDouble(latlong[0]);
                                            //Log.e("double",lat+"");
                                            Log.e("NetworkTask","15");
                                            latlong[1] = latlong[1].substring(0,idx2);
                                           // Log.e("latlong1",latlong[1]);
                                            double lag = Double.parseDouble(latlong[1]);
                                          //  chglocation(Double.parseDouble(latlong[0]),Double.parseDouble(latlong[1]));
                                            Log.e("NetworkTask","16");
                                            arr_lat.add(lat);
                                            arr_lng.add(lag);

                                            Log.e("lat",lat+"");
                                        }
                                        }
                                        }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                    Log.e("NetworkTask","17");
                    mGoogleApiClient.connect();
                    Log.e("NetworkTask","18");
                    enableMyLocation();
                    Log.e("NetworkTask","19");
//                textView.setText((CharSequence) root);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }




        // 친구에게 메시지전송 클래스
    public class viewfriendsdialog implements Runnable {


        Button btn_setdistance;
        ImageView btn_addmsg;
        CheckBox checkBox_sendloaction;

            @Override
            public void run() {
                ViewFriendDialog = new Dialog(runActivity.this);
                // 액티비티의 타이틀바를 숨긴다.
                ViewFriendDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // 커스텀 다이얼로그의 레이아웃을 설정한다.
                ViewFriendDialog.setContentView(R.layout.dialog_friends);

                // 1.온라인인 멤버 받아온 후 2.배열값 변경 후 3.라사이클러뷰에 넣기

                myfriend_adapter = new myfriendlist_Adapter(UserID,myfrindInfoArrayList,2);

                myfrd_recyclerView = ViewFriendDialog.findViewById(R.id.rc_friends);
                txt_nomsg = ViewFriendDialog.findViewById(R.id.txt_nomsg);


                if(arr_msg.size() >=1){
                    ViewFriendDialog.findViewById(R.id.txt_nomsg).setVisibility(View.INVISIBLE);
                }else{
                    txt_nomsg.setVisibility(View.VISIBLE);
                }

                btn_msgsend = (Button) ViewFriendDialog.findViewById(R.id.btn_msgsend);

                checkBox_sendloaction = ViewFriendDialog.findViewById(R.id.checkBox_sendloaction);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(runActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                myfrd_recyclerView.setLayoutManager(linearLayoutManager);
                myfrd_recyclerView.setAdapter(myfriend_adapter);

                // 라사이클러뷰에 넣기
                rc_msg = ViewFriendDialog.findViewById(R.id.rc_message);
                btn_addmsg = ViewFriendDialog.findViewById(R.id.btn_addmsg);
                for(int i =0;i<=arr_msg.size()-1;i++){
                    arr_msg.get(i).setChoice(false);
                }
                Log.e("arrmsg1",arr_msg.size()+"");
                msgAdapter = new RunMsgAdapter(arr_msg);

                LinearLayoutManager linearLayoutManager2 =  new LinearLayoutManager(runActivity.this);
                linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
                rc_msg.setLayoutManager(linearLayoutManager2);
                rc_msg.setAdapter(msgAdapter);

                btn_addmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("arrmsg2",arr_msg.size()+"");
                        new inputmsgdialog().calldialog();
                        Log.e("arrmsg3",arr_msg.size()+"");
                    }
                });


                btn_msgsend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ToUserId;
                        ToUserId = myfrindInfoArrayList.get(myfriend_adapter.getselectpos()).getId();
                        String n = null;
                        sendmsg = arr_msg.get(msgAdapter.getselpos()).getMsg();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Log.e("socketthread",Thread.currentThread().getName());
                                    sendWriter.println("msg@"+UserID +"@"+ToUserId+"@"+sendmsg+"@"+checkBox_sendloaction.isChecked());
                                  //  sendWriter.println(n);

                                    if(checkBox_sendloaction.isChecked()){
                                        sendWriter.println(lat+"@"+lng);        // 현재 좌표값
                                    }
                                    ViewFriendDialog.dismiss();
                                    sendWriter.flush();
                                    message.setText("");

                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
               ViewFriendDialog.show();

                // 서버에 친구리스트를 주고 온라인인 사람을 받아오는 스레드
               new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while(ViewFriendDialog.isShowing()){
                            try {
                                Log.e("ViewFriendDialog",Thread.currentThread().getName());
                                sendWriter.println(chkonlinemsg);
                                sendWriter.flush();
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };


            }

        }


    public void frdlistrequest(String mid){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        Log.e("frdlistrequest","1");
        String serverUrl="http://3.12.49.32/myfriendinfo.php";
        ProgressDialog progressDialog;
        /*
                progressDialog = ProgressDialog.show(runActivity.this,
                "친구 목록을 받아오는중 입니다...", null, true, true);
                progressDialog.show();
*/
         Log.e("frdlistrequest","1");
        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        Log.e("frdlistrequest","2");

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("frdlistrequest","3");
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",String.valueOf(jsonObject));
                    chkonlinemsg = "chkonline";
                    Log.e("frdlistrequest","4");

                    int fnum = jsonObject.getInt("fnum");
                    if(fnum>0){
                        Log.e("frdlistrequest","5");

                        myfrindInfoArrayList = new ArrayList<>();
                        for(int i=0;i<fnum;i++){
                            Log.e("frdlistrequest","6");
                            User user = new User();
                            user.setId(jsonObject.getString("friend"+i));
                            myfrindInfoArrayList.add(user);
                            chkonlinemsg= chkonlinemsg+"@"+jsonObject.getString("friend"+i);
                            //chkonline
                        }
                    }
                    Log.e("frdlistrequest","7");

                    Log.e("frdlistrequest","8");

                    mHandler.post(new viewfriendsdialog());
                    Log.e("frdlistrequest","9");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Log.e("frdlistrequest","10");
        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("f_id", mid);
        Log.e("frdlistrequest","11");
        // 서버에 데이터 보내고 응답 요
              RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        Log.e("frdlistrequest","13");
        requestQueue.add(smpr);
        Log.e("frdlistrequest","14");


    }


    class setonline2 implements Runnable{
        String[] onmember;

        setonline2(String[] onmember){
            this.onmember = onmember;
        }

        @Override
        public void run() {

                for(int i =0;i<=myfrindInfoArrayList.size()-1;i++){
                    myfrindInfoArrayList.get(i).setRunonline(false);
                    // 온라인 멤버 초기화
                    for(int j =1;j<=onmember.length-1;j++){
                        // onmember[0] : "chkonline"
                        if(myfrindInfoArrayList.get(i).getId().equals(onmember[j])){
                            Log.e("loginid",myfrindInfoArrayList.get(i).getId());
                            myfrindInfoArrayList.get(i).setRunonline(true);
                        }
                    }

                }


            Log.e("myfrindInfoArrayListsize",myfrindInfoArrayList.size()+"");
            // 어레이리스트 온라인인 친구 위로 정렬
            int count = 0;
            ArrayList<User> arronlilne = new ArrayList<>();
            for(int i =0;i<=myfrindInfoArrayList.size()-1;i++){
                Log.e("for문","friend");
                if(myfrindInfoArrayList.get(i).getRunonline()){
                    Log.e("if문","friend");
                    arronlilne.add(myfrindInfoArrayList.get(i));
                    myfrindInfoArrayList.remove(i);
                }
            }

            for(int i = 0;i<=arronlilne.size()-1;i++){
                myfrindInfoArrayList.add(count,arronlilne.get(i));
                count++;
            }

            myfriend_adapter.notifyDataSetChanged();
        }
    }





    class friendsloactionupdate implements Runnable{

        private String[] splited;

        public friendsloactionupdate(String[] splited) {this.splited = splited;
        }

        @Override
        public void run() {

            // 구글 맵에 표시할 마커에 대한 옵션 설정
            MarkerOptions makerOptions = new MarkerOptions();

            makerOptions.position(new LatLng(Double.parseDouble(splited[5]), Double.parseDouble(splited[6])))
                    .icon(bitmapDescriptorFromVector(runActivity.this, R.drawable.ic_baseline_email_24))
                    .title(splited[1])
                    .snippet(splited[3])
                    .alpha(0.8f);

        mMap.addMarker(makerOptions);
        }
    }



    // 메시지 받고 뷰 업데이트 메소드
    class msgUpdate implements Runnable{
        private String msg;
        private String writer;


        AlertDialog.Builder builder;

        public msgUpdate(String writer,String str) {this.writer = writer; this.msg=str;
        }

        @Override
        public void run() {

            Ringtone rt;
            Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            rt = RingtoneManager.getRingtone(getApplicationContext(),notification1);

            rt.play();

            speech( writer+"님이"+msg+"를 보냈습니다.");

            builder = new AlertDialog.Builder(runActivity.this);
            builder.setTitle(writer)        // 제목 설정
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

     public class msgboxdialog {
            Dialog dig;
            Button btn_setdistance;
            EditText pickerkm_distance;

            public void calldialog() {
                dig = new Dialog(runActivity.this);
                // 액티비티의 타이틀바를 숨긴다.
                dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // 커스텀 다이얼로그의 레이아웃을 설정한다.
                dig.setContentView(R.layout.msgboxdialog);
                
                RecyclerView my_recyclerView;
                MsgboxAdapter msgboxAdapter;

                //  리사이클러뷰 xml id
                        my_recyclerView = dig.findViewById(R.id.rc_msgbox);
                        // 라사이클러뷰에 넣기
                        // 어댑터 객체 생성

                        msgboxAdapter = new MsgboxAdapter(friendmsgArrayList,runActivity.this);

                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(runActivity.this);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        my_recyclerView.setLayoutManager(linearLayoutManager);
                        // 어댑터 추가
                        my_recyclerView.setAdapter(msgboxAdapter);

                dig.show();
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
                        if(editmsg.getText().length() == 0){
                            Toast.makeText(runActivity.this,"추가할 메시지를 작상하세요",Toast.LENGTH_SHORT);
                        }else{
                            dig.dismiss();
                            addmsg = editmsg.getText().toString();
                            Msg msg = new Msg();
                            msg.setMsg(addmsg);
                            arr_msg.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            if(arr_msg.size() >=1){
                                ViewFriendDialog.findViewById(R.id.txt_nomsg).setVisibility(View.INVISIBLE);
                            }
                        }
                        }
                    });
                dig.show();
                }
        }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}




