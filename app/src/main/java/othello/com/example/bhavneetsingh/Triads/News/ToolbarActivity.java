package othello.com.example.bhavneetsingh.Triads.News;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import othello.com.example.bhavneetsingh.Triads.R;

public class ToolbarActivity  extends AppCompatActivity {

    private Toolbar toolbar;

    protected  void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
