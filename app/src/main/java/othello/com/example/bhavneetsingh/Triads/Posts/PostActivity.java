package othello.com.example.bhavneetsingh.Triads.Posts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import othello.com.example.bhavneetsingh.Triads.Main.MainActivity;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.User.User;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String IMAGE="imageurl";
    public static final  int POSTACTIVITY=2;
    private int imgId=-1;
    EditText editText;
    private boolean edit=false;
    android.app.AlertDialog.Builder builder;
    private android.app.AlertDialog dialog = null;
    private String img;
    FloatingActionButton[]icons;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_post);
        editText=(EditText)findViewById(R.id.postText);
        Intent intent =getIntent();
        if(intent!=null)
        {
            ((TextView)findViewById(R.id.profile_name)).setText(intent.getStringExtra(User.NAME));
                    edit=intent.getBooleanExtra("edit",false);
            ImageView image=((ImageView)findViewById(R.id.profile_image));
            Picasso.get().load(intent.getStringExtra(User.PROFILE_PICTURE)).into(image);
            String start=intent.getStringExtra(Posts.KEY_CONTEXT);
            String img=intent.getStringExtra(Posts.IMAGE);
            ImageView post_image=(ImageView)findViewById(R.id.postImage);
            Picasso.get().load(img).resize(512,512).into(post_image);
            editText.setText(start);
        }
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            intent.putExtra(Posts.KEY_CONTEXT,text);
            intent.putExtra(Posts.IMAGE,img);
            intent.putExtra(Posts.KEY_SMILEY,"happy");

        }
        if(edit)
        {
          Intent get=getIntent();
          long post_id=get.getLongExtra(Posts.KEY_ID,0);
          String smiley=get.getStringExtra(Posts.KEY_SMILEY);
            intent.putExtra(Posts.KEY_SMILEY,smiley);
          intent.putExtra(Posts.KEY_ID,post_id);
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
            try{
                URL imgUrl=new URL(img);
            }
            catch (MalformedURLException e1)
            {
                img=null;
            }
        }
    }
    //open Dialog
    public void openDialog()
    {
        View convertView=null;
        if(dialog==null)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.background_select, null);
            builder.setView(convertView);
            dialog = builder.create();       dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
                            Picasso.get().load(img.toString()).into((ImageView)findViewById(R.id.postImage));
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

}