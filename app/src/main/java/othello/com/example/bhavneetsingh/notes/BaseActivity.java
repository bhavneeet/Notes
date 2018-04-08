package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public  class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout drawer;
    private MyDatabase.User current_user;
    protected  void onCreate(Bundle savedInstanceState, int layoutId, MyDatabase.User cuurent_user)
    {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        this.current_user=cuurent_user;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
/*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
*/
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView header_name=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_header);
        header_name.setText(current_user.getName());
        TextView header_id=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_id);
        header_id.setText(current_user.getUser_id());
        ImageView profile_image=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.navigation_image);
        if(current_user.getProfilePictureUrl()!=null)
        {
            Picasso.get().load(current_user.getProfilePictureUrl().toString()).resize(128,128).into(profile_image);
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {

        }
        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            logOut();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logOut()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.LOGIN,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        drawer.closeDrawer(GravityCompat.START);
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra(LoginActivity.LOGOUT,true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
