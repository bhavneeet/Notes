package othello.com.example.bhavneetsingh.Triads.News;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.Transitions.DepthTransformation;
import othello.com.example.bhavneetsingh.Triads.User.User;
import othello.com.example.bhavneetsingh.Triads.WebBrowser.WebActivity;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.onNewsClickListener {

    ArrayList<News>newsList;
    NewsAdapter newsAdapter;
    SwipeRefreshLayout refreshIcon;
    ViewPager viewPager;
    private User user;
    Toolbar toolbar;
    News current;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window=getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        /*refreshIcon=(SwipeRefreshLayout)findViewById(R.id.refresh_news);
        refreshIcon.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_red_light));
        refreshIcon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshIcon.setRefreshing(true);
                refresh();
            }
        });
   */
/*
        toolbar=findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhiteTransparent));
*/
        viewPager=(ViewPager)findViewById(R.id.news_list);
        newsList=new ArrayList<>();
        newsAdapter=new NewsAdapter(this,newsList,this);
        viewPager.setAdapter(newsAdapter);
        viewPager.setPageTransformer(true,new DepthTransformation());
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
        final AVLoadingIndicatorView progressBar=(AVLoadingIndicatorView)findViewById(R.id.rotateloading);
        DBManager.fetchNews(user, new OnDownloadComplete<ArrayList<News>>() {
            @Override
            public void onDownloadComplete(ArrayList<News> result) {
                if(result!=null)
                {
                    newsList.clear();
                    newsList.addAll(result);
                    newsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onClick(News news) {

     startWebActivity(news);
    }
    public void startWebActivity(News news)
    {
        Intent intent=new Intent(this,WebActivity.class);
        intent.putExtra(News.URL,news.getUrl());
        startActivity(intent);
    }
   /* public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu, menu);
        return  true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.linkItem)
            startWebActivity(current);
        return true;
    }*/
}