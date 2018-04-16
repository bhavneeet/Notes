package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,NavigationView.OnNavigationItemSelectedListener, ProfileAdapter.FollowListener {

    private GoogleMap mMap;
    private ArrayList<MyDatabase.User>locations;
    //private ProfileInfoWindows infoWindows;
    private ListView listView;
    private ArrayList<MyDatabase.User>users;
    private ProfileAdapter profileAdapter;
    public static final String SEARCH="search";
    private String search_key="";
    private SearchView searchView;
    boolean loading=false;
    String current_user_id;
    HashMap<String,Boolean>followers;
    ClusterManager<MapCluster>clusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        if(intent!=null)
        {
            if(intent.hasExtra(SEARCH))
            {
                search_key=intent.getStringExtra(SEARCH);
            }
        }
        followers=new HashMap<>();
        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.LOGIN,MODE_PRIVATE);
        current_user_id= Profile.getCurrentProfile().getId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loading)
                refresh();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.openDrawer(GravityCompat.START);
        toggle.syncState();

        //Adding users to list in navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        users=new ArrayList<>();
        listView=navigationView.findViewById(R.id.search_list);
        searchView=navigationView.findViewById(R.id.search_key);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(loading)
                    return false;
                search_key=query;
                refresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        progressBar=findViewById(R.id.search_progress_bar);
        profileAdapter=new ProfileAdapter(this,users,this,new MyDatabase.User(current_user_id,"",""),followers);
        profileAdapter.setItemClick(new ProfileAdapter.OnItemClick() {
            @Override
            public void onClick(MyDatabase.User user) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user.getPosition()));
            }
        });
        listView.setAdapter(profileAdapter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locations=new ArrayList<>();
    /*    infoWindows=new ProfileInfoWindows(this);
    */    mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        clusterManager=new ClusterManager<MapCluster>(getApplicationContext(),mMap);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setOnInfoWindowClickListener(clusterManager);

        // Add a marker in UserLocation and move the camera
        refresh();
    }
    private SmoothProgressBar progressBar;
    public void refresh()
    {
        mMap.clear();
        locations.clear();
        users.clear();
        loading=true;
        progressBar.setVisibility(View.VISIBLE);
        DBManager.searchUser(search_key,new OnDownloadComplete<ArrayList<MyDatabase.User>>() {
            @Override
            public void onDownloadComplete(ArrayList<MyDatabase.User> answer) {
                loading=false;
                if(answer!=null)
                {

                 final ArrayList<MyDatabase.User>result=answer;
                 DBManager.fetchFollowers(current_user_id, new OnDownloadComplete<ArrayList<MyDatabase.User>>() {
                     @Override
                     public void onDownloadComplete(ArrayList<MyDatabase.User> result1) {
                         if(result1!=null)
                         {
                             followers.clear();
                             locations.clear();
                             for(MyDatabase.User user:result1)
                             {
                                 followers.put(user.getUser_id(),true);
                             }
                             locations.addAll(result);
                             users.addAll(result);
                             profileAdapter.notifyDataSetChanged();
                             for(MyDatabase.User location:locations)
                             {
                                 clusterManager.addItem(new MapCluster(location));
                                 clusterManager.cluster();
                             }
                             if(locations!=null&&locations.size()>0)
                             mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0).getPosition()));
                         }
                         progressBar.setVisibility(View.GONE);

                     }
                 }
                 );
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.followers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClickFollowButton(View v, String current_user, final String follower) {

        final View button=v;
        if(!current_user.equals(follower))
            if(v.getAlpha()==1)
            {
                v.setAlpha((float)0.3);
                DBManager.addFollower(current_user, follower, new OnDownloadComplete<Triads.Follower>() {
                    @Override
                    public void onDownloadComplete(Triads.Follower result) {
                        if(result!=null&&result.result.equals("true"))
                        {
                            followers.put(follower,true);
                            profileAdapter.notifyDataSetChanged();
                        }
                        else {
                            button.setAlpha(1);
                            followers.remove(follower);
                            profileAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
            else
            {
                v.setAlpha(1);
                DBManager.deleteFollower(current_user,follower, new OnDownloadComplete<Triads.Follower>() {
                    @Override
                    public void onDownloadComplete(Triads.Follower result) {
                        if(result!=null&&result.result.equals("true"))
                        {
                            followers.remove(follower);
                            profileAdapter.notifyDataSetChanged();
                        }
                        else {
                            button.setAlpha((float)0.3);
                            followers.put(follower,true);
                            profileAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
    }
}