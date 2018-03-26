package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    ArrayList<Posts>newsList;
    NewsAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window=getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        ViewPager viewPager=(ViewPager)findViewById(R.id.news_list);
        newsList=new ArrayList<>();
        newsAdapter=new NewsAdapter(this,newsList);
        viewPager.setAdapter(newsAdapter);
        viewPager.setPageTransformer(true,new GateTransformation());
        Intent intent=getIntent();
        if(intent!=null)
        {
            String user_id=intent.getStringExtra(MyDatabase.User.USER_ID);
            String user_name=intent.getStringExtra(MyDatabase.User.NAME);
            String profile_picture=intent.getStringExtra(MyDatabase.User.PROFILE_PICTURE);
            String category=intent.getStringExtra(MyDatabase.User.CATEGORY);
            MyDatabase.User user=new MyDatabase.User(user_id,user_name,"");
            user.setCategory(category);
            DBManager.fetchList(user, 10, 0, new OnDownloadComplete<ArrayList<Posts>>() {
                @Override
                public void onDownloadComplete(ArrayList<Posts> result) {
                      newsList.addAll(result);
                      newsAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
