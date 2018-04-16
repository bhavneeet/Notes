package othello.com.example.bhavneetsingh.notes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class MovieDetailActivity extends AppCompatActivity {

    public  static final String ID="imdbid",CATEGORY="category";
    private String id,category;
    private SeasonFragment seasonFragment;
    private CastFragment castFragment;
    FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
               addFragment(seasonFragment);
                    return true;
                case R.id.navigation_dashboard:
                    addFragment(castFragment);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
    private MyDatabase.User user;
    private MovieListFragment movieListFragment;
    public void addFragment(android.support.v4.app.Fragment fragment)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_container,fragment,"Helo");
        fragmentTransaction.commit();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        Intent intent=getIntent();
        if(intent!=null)
        {
            String user_id=intent.getStringExtra(MyDatabase.User.USER_ID);
            String user_name=intent.getStringExtra(MyDatabase.User.NAME);
            String profile_picture=intent.getStringExtra(MyDatabase.User.PROFILE_PICTURE);
            String category=intent.getStringExtra(MyDatabase.User.CATEGORY);
            user=new MyDatabase.User(user_id,user_name,"");
            user.setCategory(category);
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        movieListFragment=MovieListFragment.newInstance(user);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(movieListFragment);
            }
        });

        /*seasonFragment=SeasonFragment.newInstance(id);
        castFragment=CastFragment.newInstance(id);
        fragmentTransaction.replace(R.id.movie_container,seasonFragment,"Helo");
        fragmentTransaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

}
