package othello.com.example.bhavneetsingh.Triads.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.MyDatabase;
import othello.com.example.bhavneetsingh.Triads.R;

public class FriendsSuggestions extends AppCompatActivity {

    private ArrayList<Icons>icons;
    private ArrayList<User>list;
    private User current_user;
    private MyDatabase db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_follows);
        Intent intent=getIntent();
        final String user_id=intent.getStringExtra(User.USER_ID);
        FollowersFragment fragment=new FollowersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.follow_container,fragment).commit();
    }

}