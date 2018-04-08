package othello.com.example.bhavneetsingh.notes;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<UserLocation>locations;
    private ProfileInfoWindows infoWindows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locations=new ArrayList<>();
        infoWindows=new ProfileInfoWindows(this);
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(infoWindows);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in UserLocation and move the camera
       DBManager.fetchLocations(new OnDownloadComplete<ArrayList<UserLocation>>() {
           @Override
           public void onDownloadComplete(ArrayList<UserLocation> result) {
               if(result!=null)
               {
                   locations.addAll(result);
                   for(UserLocation location:locations)
                   {

                       LatLng latLng = location.getPosition();
                       Marker marker=mMap.addMarker(new MarkerOptions().position(latLng).title(location.getName()).snippet(location.getName()));
                       marker.setTag(location);
                    }
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0).getPosition()));
               }
           }
       });
    }
}