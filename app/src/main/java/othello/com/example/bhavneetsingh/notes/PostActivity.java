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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String IMAGE="imageurl";
    ArrayList<Icons>icons;
    CustomAdapter arrayAdapter;
    ArrayList<String>icons_name;
    ArrayList<Integer>images;
    Spinner spinner;
    public static final  int POSTACTIVITY=2;
    private int imgId=-1;
    EditText editText;
    private boolean edit=false;
    android.app.AlertDialog.Builder builder;
    private android.app.AlertDialog dialog = null;
    private String img;
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
        spinner.setOnItemSelectedListener(this);
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
        builder=new AlertDialog.Builder(this);
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
            intent.putExtra(IMAGE,img);
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


    public void setBackground(String url)
    {
        try{
            URL imgUrl=new URL(url);
            img=url;
        }
        catch (Exception e)
        {
            img=null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)
        {
            View convertView=null;
            if(dialog==null)
            {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.background_select, null);
                builder.setView(convertView);
                dialog = builder.create();
                final View convert=convertView;
                convertView.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        {
                            String url= ((EditText)convert.findViewById(R.id.url)).getText().toString();
                            setBackground(url);
                            dialog.dismiss();
                            try{

                                URL img=new URL(url);
                                loadImage(img);
                            }
                            catch (Exception e)
                            {
                                setBackground(null);
                            }
                        }
                    }
                });
                dialog.show();
            }
            else
            {
                dialog.show();
            }
        }
    }
    public void switvhBar(boolean check)
    {

        ImageView imageView=findViewById(R.id.postImage);
        ProgressBar progressBar=findViewById(R.id.image_load);
        if(check)
        {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
         else
        {

            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
    public void loadImage(URL imgUrl)
    {
        switvhBar(true);
        Networking<URL,Bitmap>networking=new Networking<>(new InternetActivity<URL, Bitmap>() {
            @Override
            public Bitmap doInBackground(URL... args) {
                Bitmap bitmap=null;
                try{
                    URL url=args[0];
                    {
                        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        InputStream in=httpURLConnection.getInputStream();
                        bitmap= BitmapFactory.decodeStream(in);

                    }
                }
                catch (Exception e)
                {
                    bitmap=null;
                }
                finally {
                    return bitmap;
                }
            }

            @Override
            public void onPostExecute(Bitmap result) {

            }

            @Override
            public void onPreExecute(Bitmap result) {

            }
        }, new OnDownloadComplete<Bitmap>() {

            @Override
            public void onDownloadComplete(Bitmap result) {
             ImageView imageView=findViewById(R.id.postImage);
             imageView.setImageBitmap(result);
                switvhBar(false);
            }
        });
        try{
            networking.execute(imgUrl);
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}