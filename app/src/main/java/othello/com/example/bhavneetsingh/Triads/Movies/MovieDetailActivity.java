package othello.com.example.bhavneetsingh.Triads.Movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.User.User;

public class MovieDetailActivity extends AppCompatActivity {

    public  static final String ID="imdbid",CATEGORY="category";
    private String id,category;
    private SeasonFragment seasonFragment;
    private CastFragment castFragment;
    private Movie current_movie;
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
    private User user;
    private MovieListFragment movieListFragment;
    private BottomNavigationView navigation;
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
        final android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        Intent intent=getIntent();
        if(intent!=null)
        {
            String user_id=intent.getStringExtra(User.USER_ID);
            String user_name=intent.getStringExtra(User.NAME);
            String profile_picture=intent.getStringExtra(User.PROFILE_PICTURE);
            String category=intent.getStringExtra(User.CATEGORY);
            user=new User(user_id,user_name,"");
            user.setCategory(category);
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        seasonFragment=SeasonFragment.newInstance(id);
        castFragment=CastFragment.newInstance(id);
        movieListFragment=MovieListFragment.newInstance(user,new MovieAdapter.MovieSelectedListener() {
            @Override
            public void onSelectedMovie(int pos,Movie movie) {
                id=movie.getId();
                removeFragment(movieListFragment);
                navigation.setVisibility(View.VISIBLE);
                seasonFragment.setMovieId(id);
                addFragment(seasonFragment);
                castFragment.setMovieId(id);
            }
        });;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(movieListFragment);
                navigation.setVisibility(View.GONE);
            }
        });
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void removeFragment(android.support.v4.app.Fragment fragment)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
    public void addMovieFragment(android.support.v4.app.Fragment fragment)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.movie_container,fragment,"Helo");
        fragmentTransaction.commit();

    }
}
