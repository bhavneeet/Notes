package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoviesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar textView=(Toolbar) findViewById(R.id.movie_text);
        CollapsingToolbarLayout toolbar1=(CollapsingToolbarLayout) findViewById(R.id.movie_poster_toolbar);
        ImageView imageView=(ImageView) toolbar1.findViewById(R.id.movie_poster);
        Intent intent=getIntent();
        if(intent!=null)
        {/*
            Bundle bundle=intent.getExtras();
            if(bundle!=null&&bundle.containsKey(MovieAdapter.Movie.TITLE))
            {
                String text=bundle.getString(MovieAdapter.Movie.TITLE);
                String image=bundle.getString(MovieAdapter.Movie.IMAGE);
                Picasso.get().load(image).fit().into(imageView);
                textView.setTitle(text);
            }*/
        }
    }
}