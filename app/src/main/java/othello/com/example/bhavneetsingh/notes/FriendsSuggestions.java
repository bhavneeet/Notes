package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsSuggestions extends AppCompatActivity implements ProfileAdapter.FollowListener {

    private ArrayList<Icons>icons;
    private ArrayList<MyDatabase.User>list;
    private MyDatabase.User current_user;
    private MyDatabase db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_follows);
        Intent intent=getIntent();
        db_helper=MyDatabase.getInstance(this);
        current_user=DBManager.containsUser(db_helper,intent.getStringExtra(MyDatabase.User.USER_ID));
        list=DBManager.fetchUserList(db_helper,current_user);

        ProfileAdapter adapter=new ProfileAdapter(this,list,this,current_user);
        ListView listView=(ListView)findViewById(R.id.followlist);
        listView.setAdapter(adapter);
    }
    public void onClickFollowButton(Button button,int position)
    {
        boolean isFollowed =(boolean)button.getTag();
        if(isFollowed)
        {
            unfollow(position);
            button.setTag(false);
            button.setText("Follow");
        }
        else
        {
            follow(position);
            button.setTag(true);
            button.setText("UnFollow");
        }
    }
    public void follow(int position)
    {
        String source=current_user.getUser_id();
        String target=list.get(position).getUser_id();
        DBManager.addGraph(db_helper,source,target);
    }
    public void unfollow(int position)
    {
        String source=current_user.getUser_id();
        String target=list.get(position).getUser_id();
        DBManager.deleteGraphEdge(db_helper,source,target);
    }
}