package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    Intent intent;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        intent=getIntent();
        listView=(ListView)findViewById(R.id.comments_list);
         addCooments();
    }
    public void addCooments()
    {
        if(intent!=null)
        {
            Toast.makeText(this, "Comments Activty", Toast.LENGTH_SHORT).show();
            long id=intent.getLongExtra(MyDatabase.Comment.POST_ID,-1);
            if(id>=0)
            {
                MyDatabase  db=MyDatabase.getInstance(this);
                ArrayList<MyDatabase.Comment>list=DBManager.fetchCommentList(db,id);
                ArrayList<String>text=new ArrayList<>();
                for(MyDatabase.Comment comment:list)
                {
                    text.add(comment.getComment());
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.comment,R.id.comment_text,text);
                listView.setAdapter(adapter);
            }
        }
    }
}
