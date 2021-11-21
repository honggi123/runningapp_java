package com.example.myapplication.Run;


import android.os.Bundle;
import android.util.Log;
import android.view.Window;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class msgmapdialog extends AppCompatActivity implements OnMapReadyCallback {

        MapView mapView = null;

        Double lat;
        Double lng;

        private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate","oncreate");

        setContentView(R.layout.locationdialog);

        lat = getIntent().getDoubleExtra("lat",0.0);
        lng = getIntent().getDoubleExtra("lng",0.0);


        Log.e("location1",lat+":"+lng);

        mapView = (MapView) findViewById(R.id.mapview_msg);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }



    @Override
        public void onMapReady(GoogleMap googleMap) {
        Log.e("location2","map");
            mMap = googleMap;
            LatLng location = new LatLng(lat,lng);
            Log.e("location3",location.longitude+":"+location.latitude);
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions();

        makerOptions.position(location)
                .alpha(0.8f);

        mMap.addMarker(makerOptions);
        }

}
