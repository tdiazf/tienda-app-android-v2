package com.example.profundizacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.profundizacion.db.DbContactos;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private EditText textViewLatitud;
    private EditText textViewLongitud;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button buttonDeleteUser;
    private String userName, userEmail, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewLatitud = findViewById(R.id.textViewLatitud);
        textViewLongitud = findViewById(R.id.textViewLongitud);

        TextView userDetails = findViewById(R.id.textViewUserDetails);

        // En MapActivity
        email = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("userName");


        userDetails.setText("Hola " + userName + " (" + userEmail + ")");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            checkLocationSettings();
        }


        buttonDeleteUser = findViewById(R.id.buttonDeleteUser);


        // Recupera el correo electrónico y el nombre de usuario de las SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String retrievedEmail = sharedPreferences.getString("email", "");
        String userName = sharedPreferences.getString("userName", "");

        // Imprime el correo electrónico y el nombre de usuario para depuración
        Log.d("MapActivity", "Retrieved user email: " + retrievedEmail);
        Log.d("MapActivity", "Retrieved user name: " + userName);

        userDetails.setText("Hola " + userName + " (" + retrievedEmail + ")");
        buttonDeleteUser = findViewById(R.id.buttonDeleteUser);
        buttonDeleteUser.setOnClickListener(view -> deleteUser());
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());

        LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
                .addOnCompleteListener(this, task -> {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        getLastLocation();
                    } catch (ApiException exception) {
                        handleLocationSettingsException(exception);
                    }
                });
    }

    private void handleLocationSettingsException(ApiException exception) {
        switch (exception.getStatusCode()) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                    resolvable.startResolutionForResult(
                            MapActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Handle this case as per your requirement
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
                // Mostrar un mensaje al usuario explicando por qué necesita el permiso
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            getLastLocation();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        updateMapAndLocationDetails(currentLocation);
                    }
                });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        updateMapAndLocationDetails(latLng);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        updateMapAndLocationDetails(latLng);
    }

    private void updateMapAndLocationDetails(@NonNull LatLng latLng) {
        textViewLatitud.setText(String.valueOf(latLng.latitude));
        textViewLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void deleteUser() {
        try {
            DbContactos dbContactos = new DbContactos(MapActivity.this);
            Log.d("MapActivity", "Deleting user with email: " + email); // Agrega esta línea
            boolean isDeleted = dbContactos.deleteUser(email);

            if (isDeleted) {
                Toast.makeText(MapActivity.this, "USUARIO ELIMINADO", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapActivity.this, Login.class);
                startActivity(intent);
            } else {
                Toast.makeText(MapActivity.this, "ERROR AL ELIMINAR USUARIO", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(MapActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DbContactos", "Error deleting user", e);
        }
    }
}