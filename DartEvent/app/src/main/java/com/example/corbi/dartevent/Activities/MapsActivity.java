package com.example.corbi.dartevent.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.corbi.dartevent.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,  GoogleMap.OnMapLongClickListener
         {

    private GoogleMap mMap;
    private MarkerOptions markerpoint;
    private String Address;
    private LatLng latLng;
    private Geocoder geocoder;
    private FloatingActionButton returnButton;
    private String from;
    private TextView textView;
    private TextView detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkPermissions();
        from = getIntent().getExtras().getString("from");
        returnButton = findViewById(R.id.currentLocation);
        textView = findViewById(R.id.title_view);
        detailView = findViewById(R.id.detail_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(from.equals("EventCreateActivity")) {
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng current = getLocation();
                    try {
                        mMap.clear();
                        List<android.location.Address>addresses = geocoder.getFromLocation(current.latitude,current.longitude,1);
                        latLng = current;
                        Address = addresses.get(0).getAddressLine(0);//+", "+addresses.get(0).getLocality()+", "+
                        //addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName()+", "+
                        //addresses.get(0).getPostalCode()+", "+addresses.get(0).getFeatureName();
                        markerpoint = new MarkerOptions().position(current).title(Address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                        mMap.addMarker(markerpoint);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else if(from.equals("FriendEventFrag")) {
            returnButton.setVisibility(View.GONE);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this, Locale.getDefault());

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(from.equals("EventCreateActivity")) {
            mMap.setOnMapLongClickListener(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LatLng current = getLocation();
                if(current != null) {
                    try {
                        if(markerpoint!=null) {
                            markerpoint.visible(false);
                        }
                        List<android.location.Address>addresses = geocoder.getFromLocation(current.latitude,current.longitude,1);
                        latLng = current;
                        Address = addresses.get(0).getAddressLine(0);//+", "+addresses.get(0).getLocality()+", "+
                        //addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName()+", "+
                        //addresses.get(0).getPostalCode()+", "+addresses.get(0).getFeatureName();
                        markerpoint = new MarkerOptions().position(current).title(Address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                        mMap.addMarker(markerpoint);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    List<android.location.Address>addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(current.latitude,current.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address = addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getLocality()+", "+
                            addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName()+", "+
                            addresses.get(0).getPostalCode()+", "+addresses.get(0).getFeatureName();
                    markerpoint = new MarkerOptions().position(current).title(Address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    mMap.addMarker(markerpoint);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                }
            }
        }
        else if(from.equals("FriendEventFrag")) {
            String[] locString = getIntent().getExtras().getString("location").split(",");
            LatLng latLng = new LatLng(Double.parseDouble(locString[0]), Double.parseDouble(locString[1]));
            textView.setText(getIntent().getExtras().getString("title"));
            detailView.setText(getIntent().getExtras().getString("detail"));
            markerpoint = new MarkerOptions().position(latLng).title(getIntent().getExtras().getString("address")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(markerpoint);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        try {
            markerpoint.visible(false);
            mMap.clear();
            List<android.location.Address>addresses = geocoder.getFromLocation(point.latitude,point.longitude,1);
            latLng = point;
            Address = addresses.get(0).getAddressLine(0);//+", "+addresses.get(0).getLocality()+", "+
                    //addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName()+", "+
                    //addresses.get(0).getPostalCode()+", "+addresses.get(0).getFeatureName();
            markerpoint = new MarkerOptions().position(point).title(Address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(markerpoint);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LatLng getLocation() {
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location l = locationManager.getLastKnownLocation(provider);
            if(l != null) {
                LatLng latLng = new LatLng(l.getLatitude(),l.getLongitude());
                return latLng;
            } else {
                return null;
            }
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(from.equals("EventCreateActivity")) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.save_event, menu);
        }
        return true;
    }
             @Override
             public boolean onOptionsItemSelected(MenuItem item) {
                 switch (item.getItemId()) {
                     case R.id.add_to_db:
                         Intent intent = new Intent();
                         intent.setAction(EventCreateActivity.Action);
                         intent.putExtra(EventCreateActivity.AddressGetKey,Address);
                         intent.putExtra("latlng", latLng.latitude + "," + latLng.longitude);
                         sendBroadcast(intent);
                         finish();
                     default:
                         return super.onOptionsItemSelected(item);
                 }
             }

             private void checkPermissions() {
                 if (Build.VERSION.SDK_INT < 23)
                     return;

                 if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                 }
             }

             //creates the diaglog for checking permissions
             @Override
             public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED
                         || grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                 } else {
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                         if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                 || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                             //Show an explanation to the user *asynchronously*
                             AlertDialog.Builder builder = new AlertDialog.Builder(this);
                             builder.setMessage("This permission is important for the app.")
                                     .setTitle("Important permission required");
                             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                         requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                                     }

                                 }
                             });
                             requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                         } else {
                             //Never ask again and handle your app without permission.
                         }
                     }
                 }
             }

}
