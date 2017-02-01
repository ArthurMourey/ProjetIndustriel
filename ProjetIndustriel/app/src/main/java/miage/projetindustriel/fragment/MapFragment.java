package miage.projetindustriel.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miage.projetindustriel.R;
import miage.projetindustriel.connexion.DAO;
import miage.projetindustriel.model.Utilisateur;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentMapInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marqueur;
    private HashMap<String, Marker> others = new HashMap<>();
    double latitude = 0.0;
    double longitude = 0.0;

    private OnFragmentMapInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragmenet.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        List<String> pseudoKey = new ArrayList<String>();
        List<String> pseudoValue = new ArrayList<String>();
        pseudoKey.add("Pseudo");
        pseudoValue.add(Utilisateur.getPseudo());
        String listePseudo = null;
        try {
            listePseudo = DAO.post("http://www.madpumpkin.fr/pseudo.php",pseudoKey,pseudoValue);
            JSONArray json = new JSONArray(listePseudo);
            for (int i = 0; i < json.length(); i++) {
                JSONObject explrObject = json.getJSONObject(i);
                String p = explrObject.getString("Pseudo");
                getOthersPosition(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ImageButton bouton_lecture = (ImageButton) rootView.findViewById(R.id.lecture);
        bouton_lecture.setImageResource(R.drawable.lecture);

        final ImageButton bouton_precedent = (ImageButton) rootView.findViewById(R.id.precedent);
        bouton_precedent.setImageResource(R.drawable.precedent);

        final ImageButton bouton_pause = (ImageButton) rootView.findViewById(R.id.pause);
        bouton_pause.setImageResource(R.drawable.pause);

        final ImageButton bouton_suivant = (ImageButton) rootView.findViewById(R.id.suivant);
        bouton_suivant.setImageResource(R.drawable.suivant);

        bouton_lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Bouton lecture",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
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

        //setTitle("Géolocalisation");


    }


    private void getOthersPosition(String pseudo){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(pseudo);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, Double>> value = (HashMap<String, HashMap<String, Double>>) dataSnapshot.getValue();

                Double longitude = value.get("Location").get("Longitude");
                Double latitude = value.get("Location").get("Latitude");

                ajouterMarqueurOthers(dataSnapshot.getKey(),latitude,longitude);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
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

    private void ajouterMarqueurOthers(String pseudo, double latitude, double longitude) {
        LatLng coordonnees = new LatLng(latitude, longitude);
        if(others.get(pseudo) != null){
            others.get(pseudo).remove();
        }
        Marker m = mMap.addMarker(new MarkerOptions().position(coordonnees).title(pseudo).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_musique)));
        others.put(pseudo,m);
    }

    private void actualLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            ajouterMarqueur(latitude, longitude);

            //Firebase realtime database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Utilisateur.getPseudo()+"/Location/Latitude");
            myRef.setValue(latitude);
            myRef = database.getReference(Utilisateur.getPseudo()+"/Location/Longitude");
            myRef.setValue(longitude);
        }
    }

    private void maLocalisation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualLocation(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
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



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentMapInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMapInteractionListener) {
            mListener = (OnFragmentMapInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentMapInteractionListener {
        // TODO: Update argument type and name
        void onFragmentMapInteraction(String uri);
    }
}
