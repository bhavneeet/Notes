package othello.com.example.bhavneetsingh.notes;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    PostListAdapter adapter;
    final ArrayList<Posts>postsList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent=getIntent();
        if(intent.getAction().equals(Intent.ACTION_SEARCH))
        {
            String query=intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
        adapter=new PostListAdapter(this,postsList,new MainActivity());
        ListView listView =findViewById(R.id.searchList);
        listView.setAdapter(adapter);
    }
    public void doSearch(String query)
    {
        
    }
}
