package othello.com.example.bhavneetsingh.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieWebView extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CustomViewPager mViewPager;
    private ArrayList<WebViewFragment>fragments;
    private MyDatabase.User user;
    private String id;
    private ArrayList<Movie>movies;
    private int current_movie;
    private WebViewFragment[]webViewFragments;
    PopupWindow popupWindow;
    MovieAdapter movieAdapter;
    ViewPager moviePager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviewebview);
        movies=new ArrayList<>();
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
        fragments=new ArrayList<>();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.disableScroll(true);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
try {
    WebViewFragment fragment=fragments.get(mViewPager.getCurrentItem());
    fragment.getWebView().loadUrl(fragment.getCurrent_link());
}
catch (Exception e)
{
    Log.d("web",e.getLocalizedMessage());
}
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        movieAdapter=new MovieAdapter(this,movies);
        movieAdapter.notifyDataSetChanged();
        movieAdapter.setOnPageEndListener(new MovieAdapter.OnPageEndListener() {
            @Override
            public void onPageEnd(int page, final ArrayList<Movie> movies) {
             DBManager.fetchMovies(user, page, new OnDownloadComplete<ArrayList<Movie>>() {
                 @Override
                 public void onDownloadComplete(ArrayList<Movie> result) {
                    if(result!=null)
                    {
                        movies.addAll(result);
                        movieAdapter.notifyDataSetChanged();
                    }

                 }
             });
            }
        });
        movieAdapter.setMovieSelectedListener(new MovieAdapter.MovieSelectedListener() {
            @Override
            public void onSelectedMovie(int postion,Movie movie) {
                Log.d("movie_position",postion+"");
                current_movie=postion;
                chengeLinks(movie);
                popupWindow.dismiss();
            }
        });
        popupWindow=createMoviewWindow();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   moviePager=popupWindow.getContentView().findViewById(R.id.movies_list);
                   moviePager.setAdapter(movieAdapter);
                   moviePager.setPageTransformer(true,new HingeTransformation());
                   movieAdapter.notifyDataSetChanged();
                   moviePager.setCurrentItem(current_movie);
                   CoordinatorLayout view=findViewById(R.id.main_content);
                   popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

               }
               catch (Exception e)
               {
                   Log.d("Pager",e.getLocalizedMessage());
               }
            }
        });

    }
    public void addWebFragments(Movie movie)
    {
        fragments.clear();
        mSectionsPagerAdapter.notifyDataSetChanged();
        String google_link="https://www.google.co.in/search?q=";
        String google_query="";
        String wikepedia_link="https://en.m.wikipedia.org/wiki/";
        String wikepedia_query="";
        String youtube_link="https://m.youtube.com/results?q="+movie.getName();
        String onemovies_link="https://1movies.se/search_all/"+movie.getName();
        String[]arr=movie.getName().split(" ");
        for(String q:arr)
        {
            google_query+=q+"+";
            wikepedia_query+=q+"_";
        }
        google_link=google_link+google_query;
        wikepedia_link=wikepedia_link+wikepedia_query;
        webViewFragments=new WebViewFragment[4];
        String links[]={google_link,wikepedia_link,youtube_link,onemovies_link};
        for(int i=0;i<links.length;i++)
        {
            webViewFragments[i]=WebViewFragment.newInstance(links[i]);
            fragments.add(webViewFragments[i]);
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
    public void chengeLinks(Movie movie)
    {
        String google_link="https://www.google.co.in/search?q=";
        String google_query="";
        String wikepedia_link="https://en.m.wikipedia.org/wiki/";
        String wikepedia_query="";
        String youtube_link="https://m.youtube.com/results?q="+movie.getName();
        String onemovies_link="https://1movies.se/search_all/"+movie.getName();
        String[]arr=movie.getName().split(" ");
        for(String q:arr)
        {
            google_query+=q+"+";
            wikepedia_query+=q+"_";
        }
        google_link=google_link+google_query;
        wikepedia_link=wikepedia_link+wikepedia_query;
        String links[]={google_link,wikepedia_link,youtube_link,onemovies_link};
        for(int i=0;i<links.length;i++)
        {

            Bundle bundle=new Bundle();
            bundle.putString(WebViewFragment.LINK,links[i]);
            fragments.get(i).setArguments(bundle);
            if (fragments.get(i).getWebView()!=null)
            {
                fragments.get(i).setCurrent_link(links[i]);
                fragments.get(i).getWebView().clearHistory();
                fragments.get(i).getWebView().loadUrl(links[i]);

            }
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
    public void refresh()
    {
        DBManager.fetchMovies(user, new OnDownloadComplete<ArrayList<Movie>>() {
            @Override
            public void onDownloadComplete(ArrayList<Movie> result) {
                if(result!=null)
                {
                    current_movie=0;
                    movies.clear();
                    movies.addAll(result);
                    movieAdapter.notifyDataSetChanged();
                    addWebFragments(movies.get(0));
                }
            }
        });
    }
        public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<WebViewFragment>fragments;
        public SectionsPagerAdapter(FragmentManager fm,ArrayList<WebViewFragment>fragments) {
            super(fm);
            this.fragments=fragments;
        }
        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }
        }
    public void onBackPressed()
    {
        if(webViewFragments==null)
        {
            super.onBackPressed();
        }
        WebView webView=webViewFragments[mViewPager.getCurrentItem()].getWebView();
        if(webView==null)
        {
            super.onBackPressed();
        }
        else
        {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
     }
     public PopupWindow createMoviewWindow()
     {
         LayoutInflater layoutInflater=(LayoutInflater)getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);
         View contentView=layoutInflater.inflate(R.layout.movie_detail_content,null);
         Display display = getWindowManager().getDefaultDisplay();
         Point size = new Point();
         display.getSize(size);
         PopupWindow popupWindow=new PopupWindow(contentView,size.x,size.y-50,true);
         popupWindow.setOutsideTouchable(true);
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             popupWindow.setEnterTransition(new Fade());
             popupWindow.setExitTransition(new Fade());
         }
        return popupWindow;
     }
}