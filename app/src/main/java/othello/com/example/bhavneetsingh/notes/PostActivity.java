package othello.com.example.bhavneetsingh.notes;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PostActivity extends AppCompatActivity{

    ArrayList<Icons>icons;
    CustomAdapter arrayAdapter;
    ArrayList<String>icons_name;
    ArrayList<Integer>images;
    Spinner spinner;
    public static final  int POSTACTIVITY=2;
    private int imgId=-1;
    EditText editText;
    private boolean edit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        spinner=(Spinner)findViewById(R.id.options_list);
        icons_name=new ArrayList<>();
        images=new ArrayList<>();
        icons=new ArrayList<>();
        arrayAdapter=new CustomAdapter(this,icons);
        spinner.setAdapter(arrayAdapter);
        addIcons();
        editText=(EditText)findViewById(R.id.postText);
        Intent intent =getIntent();
        if(intent!=null)
        {
            ((TextView)findViewById(R.id.profile_name)).setText(intent.getStringExtra(MyDatabase.User.NAME));
                    edit=intent.getBooleanExtra("edit",false);
            String start=intent.getStringExtra("edittext");
            editText.setText(start);
        }
    }
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.post_menu,m);
        return true;
    }
    public void initializeArray()
    {
        icons_name.add(" Photo");
        icons_name.add(" BackgroundColor");
        icons_name.add(" Check in");
        icons_name.add(" Pin it");
        icons_name.add(" Camera");
        icons_name.add(" Feeling");
        icons_name.add(" Views");
        images.add(R.drawable.photo_icon);
        images.add(R.drawable.background_icon);
        images.add(R.drawable.checkin_icon);
        images.add(R.drawable.pin_icon);
        images.add(R.drawable.camera_icon1);
        images.add(R.drawable.feeling_icon);
        images.add(R.drawable.poll_icon);
        for(int i=0;i<images.size();i++)
        {
            icons.add(new Icons(icons_name.get(i),images.get(i)));
        }
        arrayAdapter.notifyDataSetChanged();
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent();
        {
            String text=editText.getText().toString();
            intent.putExtra(MyDatabase.KEY_CONTEXT,text);
            intent.putExtra(MyDatabase.KEY_SMILEY,R.drawable.smiley);
        }
        if(edit)
        {
          Intent get=getIntent();
          int position=get.getIntExtra("position",-1);
          intent.putExtra("position",position);

            setResult(MainActivity.EDITPOST,intent);
        }
        else
        {
            setResult(MainActivity.MAINACTIVITY,intent);
        }
        finish();
        return true;
    }
    public void onClick(View view)
    {

    }
    public void addIcons()
    {
        initializeArray();

    }
}