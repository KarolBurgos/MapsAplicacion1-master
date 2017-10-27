package com.example.linux.mapsaplicacion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.linux.mapsaplicacion.api.DatosApi;
import com.example.linux.mapsaplicacion.models.WifiGratis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,com.google.android.gms.maps.GoogleMap.InfoWindowAdapter {

    public static final String EXTRA_LATITUD = "";
    public static final String EXTRA_LONGITUD = "";
    private GoogleMap mMap;
    public final static String TAG ="DATOS COLOMBIA";
    private Retrofit retrofit;
    private Marker markerPais;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        retrofit=new Retrofit.Builder().baseUrl("https://www.datos.gov.co/resource/")
                .addConverterFactory(GsonConverterFactory.create()).build();

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
    public void onMapReady(final GoogleMap googleMap) {
  mMap = googleMap;


       /* //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        //boton mi ubicacion
        uiSettings.setMyLocationButtonEnabled(true);
        //uiSettings.setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(1.219586, -77.282971);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Mi casa").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        float zoomlevel=17;
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomlevel));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        //eetiquetas


       /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomlevel));

        // Cámara
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //Eventos
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnInfoWindowClickListener(this);*/


        DatosApi service=retrofit.create(DatosApi.class);
        Call<List<WifiGratis>> municipioCall=service.obtenerListaPeajes();

        municipioCall.enqueue(new Callback<List<WifiGratis>>() {
            @Override
            public void onResponse(Call<List<WifiGratis>> call, Response<List<WifiGratis>> response) {

                if(response.isSuccessful()){
                    List lista=response.body();
                    for(int i=0;i<lista.size();i++){
                        WifiGratis m=(WifiGratis) lista.get(i);
                        //Log.i(TAG,"Nombre: "+m.getAreaConstruida()+" Alcalde: "+m.getAreaTerreno());
                        LatLng sydney = new LatLng(m.getLatitud(),m.getLongitud());
                      mMap.addMarker(new MarkerOptions().position(sydney).title(m.getDireccion()).snippet(m.getDepartamento()
                      +m.getProyecto()+m.getRegion()+m.getZonaInagurada()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ww)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,5));

                       //

                        UiSettings uiSettings=mMap.getUiSettings();
                        uiSettings.setScrollGesturesEnabled(true);
                        uiSettings.setTiltGesturesEnabled(true);
                        //brujula
                        uiSettings.setCompassEnabled(true);
                        uiSettings.setZoomGesturesEnabled(true);
                        uiSettings.setRotateGesturesEnabled(true);
                        uiSettings.setAllGesturesEnabled(true);
                        uiSettings.setCompassEnabled(true);
                        uiSettings.setMyLocationButtonEnabled(true);
                        uiSettings.setMapToolbarEnabled(true);

                    }
                }
                else {
                    Log.e(TAG,"onResponse"+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<WifiGratis>> call, Throwable t) {

                Log.e(TAG,"onFailure"+t.getMessage());
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        } else {
            Button btnMiPos=(Button) findViewById(R.id.button2);
            btnMiPos.setEnabled(false);
        }

        mMap.setInfoWindowAdapter(this);


    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return prepareInfoView(marker);
    }

    private View prepareInfoView(Marker marker) {

        LinearLayout infoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MapsActivity.this);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ww,null);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);

        LinearLayout subInfoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);

        TextView subInfoLad = new TextView(MapsActivity.this);
        subInfoLad.setText(" La: " + marker.getPosition().latitude);
        TextView subInfoLad1 =  new TextView(MapsActivity.this);
        subInfoLad1.setText(" La: " + marker.getPosition().longitude);
        subInfoView.addView(subInfoLad);
        subInfoView.addView(subInfoLad1);

        infoView.addView(subInfoView);
        return  infoView;
    }




//http://www.hermosaprogramacion.com/2016/05/google-maps-android-api-v2/
}
