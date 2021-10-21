package com.example.myapplication.Run;


import android.os.Bundle;
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



public class msgmapdialog extends AppCompatActivity implements OnMapReadyCallback {

        MapView mapView = null;
        LatLng location;

        private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.locationdialog);

        //this.location = location;

        mapView = (MapView) findViewById(R.id.mapview_msg);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
    }

    @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
           //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
        }
}
