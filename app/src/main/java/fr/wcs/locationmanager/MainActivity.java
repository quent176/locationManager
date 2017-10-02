package fr.wcs.locationmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {

    LocationListener locationListener;
    LocationManager locationManager;
    private Button buttonGo;
    private TextView longitudeField;
    private TextView latitudeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitudeField = (TextView) findViewById(R.id.longitude);
        latitudeField = (TextView) findViewById(R.id.latitude);
        buttonGo = (Button) findViewById(R.id.button_go);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            latitudeField.append("\n "+location.getLatitude());
                longitudeField.append("\n "+location.getLongitude());
                Toast.makeText(MainActivity.this,"long : " + location.getLongitude() + " lat : " + location.getLatitude(),  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            configureButton();

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });

    }
}

