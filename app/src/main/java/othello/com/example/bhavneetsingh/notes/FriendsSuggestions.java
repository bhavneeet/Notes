package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsSuggestions extends AppCompatActivity {

    private ArrayList<Icons>icons;
    private ArrayList<MyDatabase.User>list;
    private MyDatabase.User current_user;
    private MyDatabase db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_follows);
        Intent intent=getIntent();
        final String user_id=intent.getStringExtra(MyDatabase.User.USER_ID);
        FollowersFragment fragment=new FollowersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.follow_container,fragment).commit();
    }

}