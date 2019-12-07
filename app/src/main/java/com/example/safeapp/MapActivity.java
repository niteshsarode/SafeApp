package com.example.safeapp;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.safeapp.data.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };



    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private boolean mDefaultGradient = true;
    private boolean mDefaultRadius = true;
    private boolean mDefaultOpacity = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button favBtn = (Button) findViewById(R.id.add_fav_btn);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        favBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //add fav endpoint
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String src_lat = getIntent().getStringExtra("src_lat");
        String src_lng = getIntent().getStringExtra("src_lng");
        String dest_lat = getIntent().getStringExtra("dest_lat");
        String dest_lng = getIntent().getStringExtra("dest_lng");

        LatLng source = new LatLng(Double.parseDouble(src_lat),Double.parseDouble(src_lng));
        LatLng destination = new LatLng(Double.parseDouble(dest_lat),Double.parseDouble(dest_lng));


        mMap.addMarker(new MarkerOptions().position(source).title("Source"));
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(source)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        Bundle bundle = getIntent().getExtras();
        String crime_arr = getIntent().getStringExtra("crime_arr");
        System.out.println("OBJECT:"+crime_arr);
        try {
            JSONArray crime_array = new JSONArray(crime_arr);
            System.out.println("JSONARRAY:"+crime_arr);
            addHeatMap(crime_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String route_arr = getIntent().getStringExtra("route_arr");
        System.out.println("OBJECT:"+route_arr);
        try {
            JSONArray route_array = new JSONArray(route_arr);
            System.out.println("JSONARRAY:"+crime_arr);
            getRoute(route_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addHeatMap(JSONArray crime_array) {
        List<LatLng> list = null;
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = { 0.2f, 1f };

        Gradient gradient = new Gradient(colors, startPoints);

        try {
            list = readItems(crime_array);
            System.out.println("LIST" + list);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        mProvider = new HeatmapTileProvider.Builder().data(list) .gradient(gradient).build();
        Log.i(getClass().getName(),mProvider.toString());
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }


    private ArrayList<LatLng> readItems(JSONArray crime_array) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        for (int i = 0; i < crime_array.length(); i++) {
            JSONObject object = crime_array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lon");

            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    private void getRoute(JSONArray route_array) throws JSONException {


        /* Draw route on MAP*/
        ArrayList points = null;
        PolylineOptions lineOptions = null;

        points = new ArrayList();
        lineOptions = new PolylineOptions();

        for (int j = 0; j < route_array.length(); j++) {
            JSONObject point = route_array.getJSONObject(j);

            double lat = Double.parseDouble(point.get("lat").toString());
            double lng = Double.parseDouble(point.get("lon").toString());
            LatLng position = new LatLng(lat, lng);

            points.add(position);
        }

        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.RED);
        lineOptions.geodesic(true);

        mMap.addPolyline(lineOptions);
    }
}
