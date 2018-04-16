package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {
    ViewPager viewPager;
    ArrayList<Movie>moviesList;
    MovieAdapter movieAdapter;
    MyDatabase.User user;

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
            String user_id=intent.getStringExtra(MyDatabase.User.USER_ID);
            String user_name=intent.getStringExtra(MyDatabase.User.NAME);
            String profile_picture=intent.getStringExtra(MyDatabase.User.PROFILE_PICTURE);
            String category=intent.getStringExtra(MyDatabase.User.CATEGORY);
            user=new MyDatabase.User(user_id,user_name,"");
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