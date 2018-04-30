package othello.com.example.bhavneetsingh.Triads.Posts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.News.*;
import othello.com.example.bhavneetsingh.Triads.User.User;

public class UserPosts extends ToolbarActivity implements UserPostsAdapter.onPostClickListener {

    private ViewPager viewPager;
    private User user;
    private ArrayList<Posts>posts;
    private UserPostsAdapter adapter;
    private Toolbar toolbar,likeToolbar;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_user_posts);
        toolbar=findViewById(R.id.toolbar);
        likeToolbar=findViewById(R.id.like_toolbar);

        likeToolbar.setVisibility(View.GONE);
        viewPager=(ViewPager)findViewById(R.id.user_posts);
        posts=new ArrayList<>();
        adapter=new UserPostsAdapter(this,posts,this);
        viewPager.setAdapter(adapter);
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
        final AVLoadingIndicatorView loading=findViewById(R.id.loading);
        DBManager.fetchList(user, 10, 0, new OnDownloadComplete<ArrayList<Posts>>() {
            @Override
            public void onDownloadComplete(ArrayList<Posts> result) {
                posts.addAll(result);
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);

            }
        });
    }
    @Override
    public void onClick(Posts posts) {
     if(likeToolbar.getVisibility()==View.VISIBLE)
     {
         changeVisibility(false);
     }
     else
     {
         changeVisibility(true);
     }
    }
    public  void changeVisibility(boolean check)
    {
        if(check)
        {
            likeToolbar.setVisibility(View.VISIBLE);
        }
        else
        {
            AlphaAnimation animation=new AlphaAnimation((float)0.5,(float)0);
            animation.setDuration(250);
            likeToolbar.setVisibility(View.GONE);

        }
    }
}
