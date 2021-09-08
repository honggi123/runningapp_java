package com.example.myapplication.Run;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runact);


        Toast.makeText(runActivity.this,"러닝시작 합니다.",Toast.LENGTH_SHORT).show();
        serviceIntent = new Intent(runActivity.this, runService.class);
        startService(serviceIntent);

        btn_run = findViewById(R.id.btn_run);
        btn_stop = findViewById(R.id.btn_stop);
        view_time = findViewById(R.id.time);
        MapView mapView = (MapView) findViewById(R.id.map);
        viewpace = findViewById(R.id.viewpace_runact);
        viewdistance = findViewById(R.id.distance_runActivity);
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

        timehandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int time = bundle.getInt("time");
                String totaltime = TimeToFormat(time);
                view_time.setText(totaltime);
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
                    stopService(serviceIntent);

                    speech("러닝을 일시 중단합니다.");
                } else {

                    btn_run.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    runstate = true;
                    restart = true;
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
        mMap.setMyLocationEnabled(true);
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
        mGoogleApiClient.connect();
        enableMyLocation();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
        fusedLocationClient.removeLocationUpdates(locationCallback);
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
        }

    }

    // googleapiclient 연결 상태가 변경 될때
    @Override
    public void onConnected(Bundle bundle) {
        enableMyLocation();
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

    private void enableMyLocation() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                locationCallback = new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location locationA =new Location("point A");
                        Location locationB =new Location("point B");

                        mCurrentLocation = locationResult.getLastLocation();

                        lat = mCurrentLocation.getLatitude();
                        lng = mCurrentLocation.getLongitude();

                        if(startset){
                            setfirstmarker(lat,lng);
                        };

                        if (runstate) {
                            locationB.setLatitude(lat);
                            locationB.setLongitude(lng);

                            endLatLng = new LatLng(lat, lng);        //현재 위치를 끝점으로 설정
                            drawPath();                                            //polyline 그리기
                            locationA.setLatitude(startLatLng.latitude);
                            locationA.setLongitude(startLatLng.longitude);
                            startLatLng = new LatLng(lat, lng);        //시작점을 끝점으로 다시 설정

                            // 거리 측정
                            distanceTo(locationA,locationB);
                        }
                    }
                };
                fusedLocationClient.requestLocationUpdates( mLocationRequest, locationCallback,Looper.myLooper() );
            }
        }
    }


        protected void createLocationRequest(){
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);    // 위치가 update되는 주기
            mLocationRequest.setFastestInterval(100);  // 위치 획득 후 update되는 주기
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


}

