package com.harsimranksaran.tripplanner.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.harsimranksaran.tripplanner.PlaceAutoCompleteAdapter;
import com.harsimranksaran.tripplanner.PlaceDetails;
import com.harsimranksaran.tripplanner.PlacesInfo;
import com.harsimranksaran.tripplanner.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MapsActivity";
    private int PLACE_PICKER_REQUEST = 1;

    private AutoCompleteTextView autoCompleteTextView;
    private ImageView searchImgview, mPlacePicker;
    private TextView placeName, placeAddress, placephoneno, placewebsite;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mPermissionsGranted = false;
    private static final int REQUEST_CODE = 131;
    private static final float DEFAULT_ZOOM = 15f;

    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient googleApiClient;
    private GeoDataClient geoDataClient;
    private PlacesInfo mPlace;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleMap mMap;
    private Location currentLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        geoDataClient = Places.getGeoDataClient(this);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_et);
        searchImgview = (ImageView)findViewById(R.id.search_view);
        placeName = (TextView)findViewById(R.id.placename);
        placeAddress = (TextView)findViewById(R.id.placeaddress);
        placephoneno = (TextView)findViewById(R.id.phoneno);
        placewebsite = (TextView) findViewById(R.id.web);
        mPlacePicker = (ImageView) findViewById(R.id.placepicker);

        getLocationPermission();

    }

    public void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if  (ActivityCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }

    // This method is used to search a place on the EditText
    private void init(){

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        autoCompleteTextView.setOnItemClickListener(mAutoCompleteListener);

        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, geoDataClient, LAT_LNG_BOUNDS, null);

        autoCompleteTextView.setAdapter(placeAutoCompleteAdapter);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                }
                return false;
            }
        });

        searchImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoLocate();
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(place.getId());
                placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
            }
        }
    }

    public void geoLocate() {
        String search = autoCompleteTextView.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();
        try{
            addressList = geocoder.getFromLocationName(search,1);
        } catch (IOException e){
            e.printStackTrace();
        }
        if (addressList.size() > 0){
            Address address = addressList.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void initMap(){
        //  Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            init();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Intent intent = new Intent(getApplicationContext(),PlaceDetails.class);
                intent.putExtra("name", mPlace.getName());
                intent.putExtra("address", mPlace.getAddress());
                intent.putExtra("id", mPlace.getId());
                intent.putExtra("rating", mPlace.getRating());
                intent.putExtra("website", mPlace.getWebsiteUri());
                intent.putExtra("phone", mPlace.getPhoneNumber());
                startActivity(intent);
                return true;
            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mPermissionsGranted = false;

        switch (requestCode){
            case REQUEST_CODE : {   // we check if the permissions are granted or not
                if (grantResults.length>0){   // if one of the permissions are granted then map will get initialized
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){ // if the permission is not granted
                            mPermissionsGranted = false;
                            return;
                        }
                    }
                    mPermissionsGranted = true;  // if the permission is granted
                    initMap();
                }
            }
        }

    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mPermissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult()!= null){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location)task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getApplicationContext(), "Turn on your location.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        } catch (SecurityException e){
            e.printStackTrace();
        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
        try {
            ;
            String place = mPlace.getName();
            String address = mPlace.getAddress();
            String contact = mPlace.getPhoneNumber();
            String site = String.valueOf(mPlace.getWebsiteUri());
            placeName.setText(place);
            placeAddress.setText(address);
            placephoneno.setText(contact);
            placewebsite.setText(site);
        } catch(Exception e){
            e.printStackTrace();
        }
        hideSoftKeyboard();
    }


    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // click handler for the suggestions
    // this sends the request for the selected location
    private AdapterView.OnItemClickListener mAutoCompleteListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = placeAutoCompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    // callback method used to get all the information about that place
    // this execute the requested location
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
            if (task.isSuccessful()) {
                PlaceBufferResponse places = task.getResult();
                final Place place = places.get(0);

                try {
                    mPlace = new PlacesInfo();
                    mPlace.setName(place.getName().toString());
                    mPlace.setAddress(place.getAddress().toString());
                    mPlace.setId(place.getId());
                    System.out.println("placeidddddddddddddddddddddd"+mPlace.getId());
                    mPlace.setAttributions(place.getAttributions().toString());
                    mPlace.setLatLng(place.getLatLng());
                    mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                    mPlace.setRating(place.getRating());
                    mPlace.setWebsiteUri(place.getWebsiteUri());

                    mPlace.setId(place.getId());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                        place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());
                places.release();

            }
        }


    };


}
