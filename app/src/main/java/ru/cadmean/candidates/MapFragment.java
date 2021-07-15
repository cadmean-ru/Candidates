package ru.cadmean.candidates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.graphics.ColorUtils;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ru.cadmean.candidates.Utils.District;
import ru.cadmean.candidates.Utils.Permissions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public MapFragment() {}
    private GoogleMap mMap;
    private static final String TAG = "MapFragment";
    private static final float DEFAULT_ZOOM = 15f;

    private View view;

    private int selectedDistrict;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frame, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);
    }

    private TextView districtText;
    private EditText mSearchText;

    private PlacesClient placesClient;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        districtText = view.findViewById(R.id.district_text);
        mSearchText = view.findViewById(R.id.input_search);

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    stopAutocomplete();
                else if (charSequence.length() > 0)
                    startAutocomplete(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autocompleteLayout = view.findViewById(R.id.autocomplete_layout);

        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
            placesClient = Places.createClient(getActivity().getApplicationContext());
        }

        view.findViewById(R.id.district_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDistrict();
            }
        });

        view.findViewById(R.id.location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserLocation();
            }
        });
    }

    private AutocompleteSessionToken autocompleteToken;
    private LinearLayout autocompleteLayout;
    private Marker locationMarker;

    private void startAutocomplete(String query) {
        if (autocompleteToken == null) {
            autocompleteToken = AutocompleteSessionToken.newInstance();
            autocompleteLayout.removeAllViews();
            autocompleteLayout.setVisibility(View.VISIBLE);
            TextView loadingText = new TextView(getContext());
            loadingText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            loadingText.setText(getString(R.string.loading));
            autocompleteLayout.addView(loadingText);
        }

        FindAutocompletePredictionsRequest req = FindAutocompletePredictionsRequest.builder()
                .setCountry("ru")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(autocompleteToken)
                .setQuery(query)
                .build();

        if (placesClient == null) return;

        placesClient.findAutocompletePredictions(req)
                .addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                        if (autocompleteToken == null) return;

                        autocompleteLayout.removeAllViews();
                        autocompleteLayout.setVisibility(View.VISIBLE);

                        List<AutocompletePrediction> predictions = findAutocompletePredictionsResponse.getAutocompletePredictions();

                        if (predictions.size() == 0) {
                            TextView noText = new TextView(getContext());
                            noText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            noText.setText(getString(R.string.nothing_found));
                            noText.setTextColor(0xCCCCCCFF);
                            noText.setPadding(0, 5, 0, 5);
                            autocompleteLayout.addView(noText);
                            return;
                        }

                        for (final AutocompletePrediction prediction : predictions) {
                            Log.i("Autocomplete", prediction.getFullText(null).toString());
                            final TextView text = new TextView(getContext());
                            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            text.setText(prediction.getFullText(null).toString());
                            text.setTextColor(0xFFFFFFFF);
                            text.setTextSize(20f);
                            text.setPadding(0, 5, 0, 5);
                            autocompleteLayout.addView(text);

                            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG);

                            FetchPlaceRequest request = FetchPlaceRequest.newInstance(prediction.getPlaceId(), placeFields);

                            placesClient.fetchPlace(request)
                                    .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                                        @Override
                                        public void onSuccess(final FetchPlaceResponse fetchPlaceResponse) {
                                            text.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    LatLng latLng = fetchPlaceResponse.getPlace().getLatLng();
                                                    Log.i("Prediction", "Clicked " + latLng.toString());
                                                    if (locationMarker != null)
                                                        locationMarker.remove();
                                                    mSearchText.setText(prediction.getFullText(null).toString());
                                                    locationMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                                                    hideSoftKeyboard();
                                                    stopAutocomplete();
                                                    selectedDistrict = getDistrictNum(latLng);
                                                    setSelectedDistrict();
                                                }
                                            });
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Autocomplete", "Failed to find predictions");
                        autocompleteLayout.removeAllViews();
                        autocompleteLayout.setVisibility(View.VISIBLE);
                        TextView errorText = new TextView(getContext());
                        errorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        errorText.setTextColor(0xCCCCCCFF);
                        errorText.setPadding(0, 5, 0, 5);
                        errorText.setText(getString(R.string.error));
                        autocompleteLayout.addView(errorText);
                    }
                });
    }

    private void setSelectedDistrict() {
        if (selectedDistrict == 0)
            districtText.setText(getString(R.string.district_not_found));
        else
            districtText.setText(getString(R.string.district_number, selectedDistrict));
    }

    private void confirmDistrict() {
        if (selectedDistrict != 0) {
            getActivity().getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putInt("district", selectedDistrict)
                    .apply();
        }

        String message;
        if (selectedDistrict == 0) {
            message = getString(R.string.district_not_found);
        } else {
            message = getString(R.string.active_district, selectedDistrict);
        }

        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(view).navigate(R.id.action_nav_map_to_nav_district);
                    }
                })
                .create()
                .show();
    }

    private void stopAutocomplete() {
        autocompleteToken = null;
        autocompleteLayout.removeAllViews();
        autocompleteLayout.setVisibility(View.GONE);
    }

    private void hideSoftKeyboard(){
        ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private final ArrayList<District> districts = new ArrayList<>();

    private int getDistrictNum(LatLng latLng) {
        for (District d : districts) {
            if (d.checkCoordinates(latLng)) return d.getNumber();
        }
        return 0;
    }

    private FusedLocationProviderClient fusedLocationClient;

    private void getUserLocation() {
        if (!Permissions.checkAndRequestPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) return;

        if (fusedLocationClient == null)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (locationMarker != null)
                            locationMarker.remove();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        locationMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                        selectedDistrict = getDistrictNum(latLng);
                        setSelectedDistrict();
                        confirmDistrict();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String data = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.mosio);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            data = sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (data == null) return;
        JSONObject json = null;

        try {
            json = new JSONObject(data);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        if (json == null) return;

        GeoJsonLayer geoJsonLayer = new GeoJsonLayer(mMap, json);

        for (GeoJsonFeature f : geoJsonLayer.getFeatures()) {
            GeoJsonPolygonStyle geoJsonPolygonStyle = new GeoJsonPolygonStyle();

            Random r = new Random();
            int color = r.nextInt(0xFFFFFF);
            int colorTransparent = ColorUtils.setAlphaComponent(color, 100);

            geoJsonPolygonStyle.setFillColor(colorTransparent);

            f.setPolygonStyle(geoJsonPolygonStyle);


            List<LatLng> coordinates;

            try {
                coordinates = ((GeoJsonPolygon)f.getGeometry()).getCoordinates().get(0);
            } catch (ClassCastException ex) {
                try {
                    coordinates = ((GeoJsonMultiPolygon)f.getGeometry()).getPolygons().get(0).getCoordinates().get(0);
                } catch (ClassCastException ex1) {
                    continue;
                }
            }
            districts.add(new District(Integer.parseInt(f.getProperty("IO_ID")), coordinates));
        }

        geoJsonLayer.addLayerToMap();


        geoJsonLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                selectedDistrict = Integer.parseInt(feature.getProperty("IO_ID"));
                setSelectedDistrict();
                confirmDistrict();
            }
        });

        LatLng moscow = new LatLng(55.751244, 37.618423);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(moscow, 9f);
        mMap.animateCamera(update);
    }
}
