package othello.com.example.bhavneetsingh.Triads.Movies;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.User.User;

public class MoviesActivity extends AppCompatActivity {
    ViewPager viewPager;
    ArrayList<Movie>moviesList;
    MovieAdapter movieAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
       */ /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar textView=(Toolbar) findViewById(R.id.movie_text);
        CollapsingToolbarLayout toolbar1=(CollapsingToolbarLayout) findViewById(R.id.movie_poster_toolbar);
        ImageView imageView=(ImageView) toolbar1.findViewById(R.id.movie_poster);*/
        viewPager=(ViewPager)findViewById(R.id.movies_pager);
        moviesList=new ArrayList<>();
        movieAdapter=new MovieAdapter(this,moviesList);
        viewPager.setAdapter(movieAdapter);
        /*viewPager.setPageTransformer(true,new CubeOutRotationTransformation());
        *///init

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
        refresh();
    }
    public void refresh()
    {
        final AVLoadingIndicatorView progressBar=(AVLoadingIndicatorView)findViewById(R.id.movierotateloading);
        DBManager.fetchMovies(user, new OnDownloadComplete<ArrayList<Movie>>() {
            @Override
            public void onDownloadComplete(ArrayList<Movie> result) {
                if(result!=null)
                {
                    moviesList.clear();
                    moviesList.addAll(result);
                    movieAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    FrameLayout frameLayout=(FrameLayout)findViewById(R.id.movie_root);
                    frameLayout.setBackground(getDrawable(R.color.colorWhite));
                }
            }
        });
    }
    public void fetchSeasons(String link)
    {

    }
}