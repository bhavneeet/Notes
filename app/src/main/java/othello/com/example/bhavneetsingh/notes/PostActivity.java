package othello.com.example.bhavneetsingh.notes;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String IMAGE="imageurl";
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
    FloatingActionButton[]icons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new);
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
        icons=new FloatingActionButton[3];
        icons[0]=(FloatingActionButton)findViewById(R.id.edit_icon);
        icons[1]=(FloatingActionButton)findViewById(R.id.camera_icon);
        icons[2]=(FloatingActionButton)findViewById(R.id.photo_icon);
        AlphaAnimation animation=new AlphaAnimation((float)0,1);
        animation.setDuration(1000);
        for(FloatingActionButton but:icons)
        {
            but.setAnimation(animation);
            but.setOnClickListener(this);
        }
    }
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.post_menu,m);
        return true;
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
    public void makeVisible(boolean check)
    {
        if(check)
        {
            AlphaAnimation animation=new AlphaAnimation((float)0,1);
            animation.setDuration(250);
            AlphaAnimation animation1=new AlphaAnimation((float)0,1);
            animation.setDuration(500);
            icons[1].startAnimation(animation);
            icons[2].startAnimation(animation1);
            icons[1].setVisibility(View.VISIBLE);
            icons[2].setVisibility(View.VISIBLE);
        }
        else
        {
            AlphaAnimation animation=new AlphaAnimation((float)1,0);
            animation.setDuration(250);
            AlphaAnimation animation1=new AlphaAnimation((float)1,0);
            animation.setDuration(500);
            icons[1].startAnimation(animation1);
            icons[2].startAnimation(animation);
            icons[1].setVisibility(View.GONE);
            icons[2].setVisibility(View.GONE);
        }
    }
    private boolean fabOpen=false;
    public boolean fabOpen()
    {
        return icons[1].getVisibility()==View.VISIBLE;
    }
    public void onClick(View view)
    {
     if(view.getId()==R.id.edit_icon)
     {
         if(!fabOpen())
         {
             makeVisible(true);
         }
         else
         {
          makeVisible(false);
         }

     }
     else if(view.getId()==R.id.photo_icon)
     {
         openDialog();
     }
     else if(view.getId()==R.id.camera_icon)
     {

     }
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
    public static AlertDialog dialogBuilder(Context context)
    {
        AlertDialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.background_select, null);
        builder.setView(convertView);
        dialog = builder.create();
        return dialog;
    }
    //open Dialog
    public void openDialog()
    {
        View convertView=null;
        if(dialog==null)
        {
            dialog=dialogBuilder(this);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
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
    public void switchBar(boolean check)
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
        switchBar(true);
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
                switchBar(false);
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

}