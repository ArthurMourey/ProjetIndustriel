package miage.projetindustriel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marqueur;
    double latitude = 0.0;
    double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        maLocalisation();

        setTitle("Géolocalisation");

        /*double latitude = 0;
        double longitude = 0;
        LatLng artem = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(artem).title("Vous êtes ici !"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(artem));
        */

        final ImageButton bouton_map = (ImageButton) findViewById(R.id.button_map_map);
        bouton_map.setImageResource(R.drawable.geolocalisation);

        final ImageButton bouton_music = (ImageButton) findViewById(R.id.button_music_map);
        bouton_music.setImageResource(R.drawable.musique);
        bouton_music.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_disconnect = (ImageButton) findViewById(R.id.button_disconnect_map);
        bouton_disconnect.setImageResource(R.drawable.deconnexion);
        bouton_disconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, Login.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_lecture = (ImageButton) findViewById(R.id.lecture);
        bouton_lecture.setImageResource(R.drawable.lecture);

        final ImageButton bouton_precedent = (ImageButton) findViewById(R.id.precedent);
        bouton_precedent.setImageResource(R.drawable.precedent);

        final ImageButton bouton_pause = (ImageButton) findViewById(R.id.pause);
        bouton_pause.setImageResource(R.drawable.pause);

        final ImageButton bouton_suivant = (ImageButton) findViewById(R.id.suivant);
        bouton_suivant.setImageResource(R.drawable.suivant);
    }

    private void ajouterMarqueur(double latitude, double longitude) {
        LatLng coordonnees = new LatLng(latitude, longitude);
        CameraUpdate zoomLocation = CameraUpdateFactory.newLatLngZoom(coordonnees, 16);
        if (marqueur != null) {
            marqueur.remove();
        }
        marqueur = mMap.addMarker(new MarkerOptions().position(coordonnees).title("Vous êtes ici !").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_musique)));
        mMap.animateCamera(zoomLocation);
    }

    private void actualLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            ajouterMarqueur(latitude, longitude);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void maLocalisation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualLocation(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
    }

    private void utilisateursProches(){
        //Récupération de tous les utilisateurs
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<>();
        for(Utilisateur u : listeUtilisateur){
            //Calcul de distance
            //Si pas proche : afficher avec un marqueur
        }
    }
}
