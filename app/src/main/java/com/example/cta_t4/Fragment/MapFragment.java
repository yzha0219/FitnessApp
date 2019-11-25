package com.example.cta_t4.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.HttpUrlConnection;
import com.example.cta_t4.R;
import com.example.cta_t4.entity.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    View mView;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Integer userId;
    User user;
    String url;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment, container, false);
        userId = ((MainActivity) getActivity()).getLogin_user().getUserId();
        user = ((MainActivity) getActivity()).getLogin_user();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        List<String> locList = null;
        Object[] resource = null;

        googleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        sb.append("query=" + user.getAddress() + "+" + user.getPostcode());
        sb.append("&key=" + "AIzaSyAeEy_OgxDefJCNpIQX58H7Jbd3qTisNZw");
        String url = sb.toString();
        Object dt[] = new Object[2];
        dt[0] = googleMap;
        dt[1] = url;
        LocationAsyn locationAsyn = new LocationAsyn();
        ParksAsyn ParksAsyn = new ParksAsyn();
        try {
            locList = locationAsyn.execute(dt).get();
            resource = park(Double.parseDouble(locList.get(0)), Double.parseDouble(locList.get(1)));
            ParksAsyn.execute(resource);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public class LocationAsyn extends AsyncTask<Object, String, List<String>> {
        @Override
        protected List<String> doInBackground(Object... objects) {
            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            List<String> infoList = new ArrayList<>();
            infoList.add(new HttpUrlConnection().getHttpUrlConnection(url));
            infoList.add(user.getAddress());
            infoList.add(String.valueOf(user.getPostcode()));
            String add_lat = null;
            String add_long = null;
            List<String> locList = new ArrayList<String>();
            try {
                JSONObject resultJson = new JSONObject(infoList.get(0));
                JSONArray resultArray = resultJson.getJSONArray("results");
                JSONObject locationJSON = resultArray.getJSONObject(0);
                JSONObject location = locationJSON.getJSONObject("geometry").getJSONObject("location");
                add_lat = location.getString("lat");
                add_long = location.getString("lng");
                locList.add(add_lat);
                locList.add(add_long);
                return locList;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return locList;
        }

        @Override
        protected void onPostExecute(List<String> resultList) {

        }
    }

    public Object[] park(Double latitude, Double longitude) {
        LatLng HOME = new LatLng(latitude, longitude);
        MarkerOptions mHome = new MarkerOptions()
                .position(HOME)
                .title("Home");
        googleMap.addMarker(mHome);
        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(HOME)
                .radius(5000)
                .strokeColor(Color.GRAY));
        LatLng rightBottom = SphericalUtil.computeOffset(HOME, 5000, 135);
        LatLng leftTop = SphericalUtil.computeOffset(HOME, 5000, -45);
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(rightBottom.latitude, leftTop.longitude), new LatLng(leftTop.latitude, rightBottom.longitude));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 0);
        googleMap.moveCamera(cameraUpdate);
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("&location=" + latitude + "," + longitude);
        sb.append("&radius=" + 5000);
        sb.append("&types=" + "park");
        sb.append("&key=" + "AIzaSyAeEy_OgxDefJCNpIQX58H7Jbd3qTisNZw");
        String url = sb.toString();
        Object resource[] = new Object[2];
        resource[0] = googleMap;
        resource[1] = url;
        return resource;
    }

    public class ParksAsyn extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            return new HttpUrlConnection().getHttpUrlConnection(url);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject parentObject = new JSONObject(s);
                JSONArray resultArray = parentObject.getJSONArray("results");

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
                    String latitude = locationObj.getString("lat");
                    String longitude = locationObj.getString("lng");
                    String park_name = jsonObject.getString("name");
                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng)
                            .title(park_name);
                    googleMap.addMarker(markerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest().create();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
