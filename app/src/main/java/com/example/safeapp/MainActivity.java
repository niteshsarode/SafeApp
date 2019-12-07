package com.example.safeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.location.LocationListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location final_location = null;
    String destination_value = "";
    Button route_btn;
    EditText destination_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText source_text = (EditText)findViewById(R.id.source);
        String source_value = source_text.getText().toString();
        destination_text = (EditText)findViewById(R.id.destination);
        route_btn = (Button) findViewById(R.id.get_route_btn);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            } else {
                //request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                //request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

            }
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
                    // You need to call this whenever you are done:
                    // mLocationManager.removeUpdates(this);
                }
            }
            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude","disable");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude","enable");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Latitude","status");
            }
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //TO DO: check GPS and NETWORK provider
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            final_location = location;
//            route_btn.setEnabled(true);
            route_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        destination_value = destination_text.getText().toString();
                        String url = "http://35.232.208.226/data/route_solver?source_lat="+final_location.getLatitude()+"&source_lon="+final_location.getLongitude()+"&dest_addr="+destination_value;
                        System.out.println("URL:"+url);
                        String response = new RouteUrl().execute(url).get();
                        JSONObject responseObject = new JSONObject(response);
                        System.out.print("Response"+responseObject);
                        String dest_lat = responseObject.get("dest_lat").toString();
                        String dest_lon = responseObject.get("dest_lon").toString();
                        JSONArray crime_arr = responseObject.getJSONArray("crime_arr");
                        JSONArray route_arr = responseObject.getJSONArray("route_arr");

                        Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
                        System.out.println("SOURCE LOCATION:"+final_location);
                        mapIntent.putExtra("src_lat",Double.toString(final_location.getLatitude()));
                        mapIntent.putExtra("src_lng",Double.toString(final_location.getLongitude()));
                        mapIntent.putExtra("dest_lat",dest_lat);
                        mapIntent.putExtra("dest_lng",dest_lon);
                        mapIntent.putExtra("crime_arr",crime_arr.toString());
                        mapIntent.putExtra("route_arr",route_arr.toString());
                        startActivity(mapIntent);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
