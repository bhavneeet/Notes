package othello.com.example.bhavneetsingh.notes;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.Notification;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import pl.droidsonroids.gif.GifImageView;

import static othello.com.example.bhavneetsingh.notes.LoginActivity.LOGIN;
import static othello.com.example.bhavneetsingh.notes.LoginActivity.NULL;

public class MainActivity extends AppCompatActivity implements  PostListAdapter.OnClickIcon,PostListAdapter.OnClicks,View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static int id = 3;
    public static final int MAINACTIVITY = 1;
    private final ArrayList<Posts> postList= new ArrayList<>();;
    private PostListAdapter adapter;
    private  ListView list;
    private EditText editText;
    //for Profile Photo
    PopupWindow profilePic;
    private MyDatabase db_helper;//My Databse
    private Bundle user_bundle;
    private DrawerLayout drawer;
    private ProgressBar bar;
    private MyDatabase.User current_user;
    private final HashMap<String,Integer>followers=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Setting progressbar
        // Get the intent, verify the action and get the query
        Intent intent1 = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent1.getAction())) {
            String query = intent1.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db_helper =  MyDatabase.getInstance(this);
        Intent intent=getIntent();
        //Getting user profile from login activity
        if(intent!=null&&intent.hasExtra(MyDatabase.User.USER_ID))
        {
            user_bundle=intent.getExtras();
            current_user=DBManager.containsUser(db_helper,user_bundle.getString(MyDatabase.User.USER_ID));
        }
        else {
            login();
        }
        //Following maps
        DBManager.getFollowers(current_user.getUser_id(), new OnDownloadComplete<HashMap<String, Integer>>() {
            @Override
            public void onDownloadComplete(HashMap<String, Integer> result) {
                followers.clear();
                followers.putAll(result);
            }
        });
        //
        TextView header_name=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_header);
        header_name.setText(current_user.getName());
        TextView header_id=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_id);
        header_id.setText(current_user.getUser_id());
        //For Welcoming Toast
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        editText = (EditText) findViewById(R.id.postText);
        adapter = new PostListAdapter(this, postList, this);
        adapter.setOnClicks(this);
        bar=(ProgressBar)findViewById(R.id.progressBar);
        list = (ListView) findViewById(R.id.allPosts);
        list.setAdapter(adapter);
        list.setClickable(true);
        switcBars(true);
        refresh();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(this);
        //Initializing Popup menu
        profilePic=new PopupWindow(this);
    }
    //storing followerlist
    //login
    public void login()
    {
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences(LoginActivity.LOGIN,MODE_PRIVATE);
        String userid=sharedPreferences.getString(LoginActivity.LOGIN,LoginActivity.NULL);
        current_user=DBManager.containsUser(db_helper,userid);
    }
    public void onStart()
    {
        super.onStart();
        BroadcastReceiver receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Recieved", Toast.LENGTH_SHORT).show();
            }
                };
        IntentFilter intentFilter=new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        intentFilter.addAction(Telephony.Sms.Intents.SMS_DELIVER_ACTION);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);

    }
    public void onStop()
    {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            fetchMyNotes();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            logOut();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Logging out
    public void logOut()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.LOGIN,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        drawer.closeDrawer(GravityCompat.START);
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra(LoginActivity.LOGOUT,true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    //fetch only my notes
    public void fetchMyNotes()
    {
      postList.clear();
       postList.addAll(DBManager.fetchMyNotes(db_helper,current_user));
       adapter.notifyDataSetInvalidated();
       adapter.notifyDataSetChanged();
    }
    //Showing Popup for Following
    private void showFollowPopup()
    {
     Intent intent=new Intent(this,FriendsSuggestions.class);
     intent.putExtra(MyDatabase.User.USER_ID,current_user.getUser_id());
        startActivity(intent);

    }
    public void onClickFollowButton(Button button)
    {
        button.setText("Unfollow");
    }
    //To give different Ids to class
    public static int getClassId() {
        return ++id;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_post_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.searchItem).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        ;
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()<3)
                    return false;
                MyDatabase.User current=DBManager.containsUser(db_helper,query);
                ArrayList<Posts>list=DBManager.fetchSimilarNotes(db_helper,query);
                Toast.makeText(MainActivity.this, list.size()+"", Toast.LENGTH_SHORT).show();

                if(list.size()>0)
                {
                   postList.clear();
                   postList.addAll(list);
                   adapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh)
        {
            switcBars(true);
            refresh();
        }
        else if(item.getItemId()==R.id.sugestions)
        {
            showFollowPopup();
        }
        else if(item.getItemId()==R.id.searchItem)
        {
        }
        return true;
    }
    //Switching progressbar and list
    public void switcBars(boolean check)
    {
        if(check)
        {
            list.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);

        }
        else
        {
            list.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);

        }
    }
    //Refreshing
    private void refresh()
    {
              DBManager.fetchList(current_user, new OnDownloadComplete<ArrayList<Posts>>() {
            @Override
            public void onDownloadComplete(ArrayList<Posts> result) {
              if(result!=null)
              {
                  postList.clear();
                  postList.addAll(result);
                  adapter.notifyDataSetChanged();
                  switcBars(false);
                  //for loading profilepictres
                  ArrayList<URL>urls=new ArrayList<>();
                  for(int i=0;i<postList.size();i++)
                  {
                      urls.add(postList.get(i).getUser().getProfilePictureUrl());
                  }
                  loadImages(urls,new OnDownloadComplete<ArrayList<Bitmap>>() {
                      @Override
                      public void onDownloadComplete(ArrayList<Bitmap> result) {
                          addProfileImages(result);
                      }
                  });
                  //For loading images
                  urls.clear();
                  for(int i=0;i<postList.size();i++)
                  {
                      urls.add(postList.get(i).getImgUrl());
                  }
                  loadImages(urls,new OnDownloadComplete<ArrayList<Bitmap>>() {
                      @Override
                      public void onDownloadComplete(ArrayList<Bitmap> result) {
                          addImages(result);
                      }
                  });
                  adapter.notifyDataSetChanged();
              }
            }
        });

    }
/*
* When Post is created then result from post activity is added to list in main activity*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PostActivity.POSTACTIVITY) {
            if (resultCode == MAINACTIVITY) {
                if (data != null) {

                    String url=data.getStringExtra(PostActivity.IMAGE);
                    String text = data.getStringExtra(MyDatabase.KEY_CONTEXT);
                    int smileyId = data.getIntExtra(MyDatabase.KEY_SMILEY, R.drawable.smiley);
                    Bundle bundle = new Bundle();
                    bundle.putString(MyDatabase.KEY_CONTEXT, text);
                    bundle.putInt(MyDatabase.KEY_SMILEY, smileyId);
                    switcBars(true);
                    //Storing new Post in Databse
                     DBManager.addPost(current_user,text, new OnDownloadComplete<Posts>() {
                        @Override
                        public void onDownloadComplete(Posts result) {
                           if(result!=null)
                           {
                               postList.add(0,result);
                               refresh();
                               switcBars(false);
                           }
                        }
                    });

                }
            }
            else if (resultCode == EDITPOST)
            {
                if (data != null) {

                    String text = data.getStringExtra(MyDatabase.KEY_CONTEXT);
                    int smileyId = data.getIntExtra(MyDatabase.KEY_SMILEY, R.drawable.smiley);
                    int position = data.getIntExtra("position", -1);
                    String url=data.getStringExtra(PostActivity.IMAGE);
                    Bundle bundle = new Bundle();
                    if (position >= 0) {
                        postList.get(position).setContent(text);
                        long id = postList.get(position).getId();
                        Posts post = postList.get(position);
                        //Storing existing Post in Databse
                        switcBars(true);
                        postList.get(position).setContent(text);
                        try
                        {
                            postList.get(position).setImgUrl(url);
                        }
                        catch(Exception e)
                        {

                        }

                        DBManager.editPost(post, new OnDownloadComplete<Posts>() {
                            @Override
                            public void onDownloadComplete(Posts result) {
                                refresh();
                            switcBars(false);
                            }
                        });

                    }
                }
            }
        }
    }
    public static final int EDITPOST=10;
    //When profile photo is clicked
    private View convertView;
    @Override
    public void onClickIcon(View view,int position) {

        {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.icon_view, list,false);
                //Setting Proflie Image
                profilePic.setContentView(convertView);
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 profilePic.setEnterTransition(new Slide());
                 profilePic.setOverlapAnchor(false);
                 profilePic.setExitTransition(new Slide());
            }

            View v=((ImageView)convertView.findViewById(R.id.follow));
            String user_id=postList.get(position).getUser().getUser_id();
            if(!current_user.getUser_id().equals(postList.get(position).getUser().getUser_id()))
            {
                if(followers.containsKey(user_id))
                {
                    v.setAlpha((float)1);
                }
                else {
                    v.setAlpha((float) 0.5);
                }
            }
            else
            {
                v.setAlpha((float)0.5);
            }
            ImageView imageView=(ImageView)convertView.findViewById(R.id.icon_image);
            if(postList.get(position).getUser().getProfilePicture()!=null)
            (imageView).setImageBitmap(postList.get(position).getUser().getProfilePicture());
            ((TextView)convertView.findViewById(R.id.icon_title)).setText(postList.get(position).getUser().getName());
            final int pos=position;
            ((ImageView)convertView.findViewById(R.id.follow)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow(v,pos);
                }
            });
            profilePic.setElevation(20);
            profilePic.setFocusable(true);
            View root=findViewById(R.id.content_home);
            profilePic.showAtLocation(root,Gravity.CENTER,0,0);
        }
    }
    //Searching in UsersList

    //Following process
    public void follow(View v,int position)
    {
        String user_id=postList.get(position).getUser().getUser_id();
        if(!current_user.getUser_id().equals(postList.get(position).getUser().getUser_id()))

        {
            if(followers.containsKey(user_id))
        {
            v.setAlpha((float)1);
            followers.remove(user_id);
            DBManager.deleteFollower(current_user.getUser_id(),user_id);
        }
        else {
            followers.put(user_id,1);
            v.setAlpha((float)0.5);
            DBManager.addFollower(current_user.getUser_id(),user_id);
        }}
    }
    //When menuitem of posts is clicked
    public void onClickPopupMenu(MenuItem menuItem, int pos) {
        Log.d("currentUser",postList.get(pos).getUser().getUser_id()+" "+current_user.getUser_id());
        if(!postList.get(pos).getUser().getUser_id().equals(current_user.getUser_id()))
        {
            Toast.makeText(this, "Not Your Property", Toast.LENGTH_SHORT).show();
            return;
        }
        if(menuItem.getItemId() == R.id.editMenuItem) {

            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra(MyDatabase.User.NAME,current_user.getName());
            String text = postList.get(pos).getContent();
            intent.putExtra("edittext", text);
            intent.putExtra("position", pos);
            intent.putExtra("edit",true);
            startActivityForResult(intent, PostActivity.POSTACTIVITY);
        }
        else if(menuItem.getItemId()==R.id.deleteMenuItem)
        {
                if(postList.size()>0){
                    Posts posts=postList.get(pos);
                    Log.d("error",pos+"");
                    DBManager.delete(posts.getId(), new OnDownloadComplete<Posts>() {
                        @Override
                        public void onDownloadComplete(Posts result) {
                        }
                    });
                    postList.remove(pos);
                    adapter.notifyDataSetChanged();
            }
        }
        else if(menuItem.getItemId()==R.id.reportMenuItem)
        {
            Toast.makeText(this,"Reported",Toast.LENGTH_LONG).show();
        }
    }

    //See Like button is clicked or not
    public void onClickLikeButton(View context, int id,int pos) {
        if (window != null) {
            if (!window.isShowing()) {
            }
        }
        if (id == R.id.like_button) {
           GifImageView but=(GifImageView)(context.findViewById(id));
            onLikeClick(context,but,pos);
        }
        else if (id == R.id.comment_button) {
            ImageButton button = (ImageButton) (context.findViewById(id));
            onCommentClick(context,button,pos);
        }
        else if (id== R.id.share_button) {
         sharePost(context,pos);

        }
    }
    //Shsre current post
    public void sharePost(View context,int pos)
    {
        Intent intent=new Intent();
        MyDatabase.User user=postList.get(pos).getUser();
        String text=user.getName()+"\n"
                +postList.get(pos).getContent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(intent,text));
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    private boolean likeClicked = false;
    private PopupWindow window;
  //On clicking of like button
    public void onLikeClick(View context, final GifImageView button, final int pos) {
        int winHeight=0;
        if (window == null) {
            window = new PopupWindow(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.like_options, null);
            winHeight=view.getHeight();
            window.setContentView(view);
        }
        final Posts posts=postList.get(pos);
        int height = button.getHeight();
        window.setBackgroundDrawable(getResources().getDrawable(R.color.colorWhite));
        View view = window.getContentView();
        final HashMap<Integer, Integer> map = new HashMap<>();
        map.put(R.id.like_options_happy, R.drawable.laugh);
        map.put(R.id.like_options_angry, R.drawable.angry);
        map.put(R.id.like_options_sad, R.drawable.sad);
        map.put(R.id.like_options_thuglife, R.drawable.happy);
        for (Integer key : map.keySet()) {
            final int k = key;
            view.findViewById(key).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setImageResource(map.get(k));
                    posts.setSmiley_id(map.get(k));
                    DBManager.editPost(posts, new OnDownloadComplete<Posts>() {
                        @Override
                        public void onDownloadComplete(Posts result) {
                        }
                    });
                    window.dismiss();
                }
            });
        }
        window.setFocusable(true);
        window.getContentView().setOnClickListener(this);
        int offy = (int) dpToPx(50)+height;
        window.showAsDropDown(button, 40, -(offy));
        likeClicked = true;
    }
   //Creating Post
    public void loadImages(ArrayList<URL>images,OnDownloadComplete<ArrayList<Bitmap>>downloadComplete)
    {
        Networking<URL,ArrayList<Bitmap>>networking=new Networking<>(new InternetActivity<URL, ArrayList<Bitmap>>() {

            @Override
            public ArrayList<Bitmap> doInBackground(URL... args) {
                ArrayList<Bitmap>bitmaps=new ArrayList<>();
                for(URL url:args)
                {  try{
                    bitmaps.add(getImage(url));
                }
                catch (Exception e)
                {
                   bitmaps.add(null);
                }
             }
             return bitmaps;
            }

            @Override
            public void onPostExecute(ArrayList<Bitmap> result) {

            }

            @Override
            public void onPreExecute(ArrayList<Bitmap> result) {

            }
        },downloadComplete);
        URL[] arr=new URL[postList.size()];
        for(int i=0;i<postList.size();i++)
            arr[i]=images.get(i);
        networking.execute(arr);
    }
    public void addImages(ArrayList<Bitmap>result)
    {
        for(int i=0;i<result.size();i++){

            postList.get(i).setBitmap(result.get(i));
        }
        adapter.notifyDataSetChanged();

    }
    private void addProfileImages(ArrayList<Bitmap>result)
    {
        for(int i=0;i<result.size();i++){

            postList.get(i).getUser().setProfilePicture(result.get(i));
        }
        adapter.notifyDataSetChanged();

    }
    public void onClick(View view) {
        if(view.getId()==R.id.fab)
        {
            createPost();
        }

    }
    private PopupWindow commentWindow=null;
    public void onCommentClick(View context,ImageButton button,int pos)
    {

        //When Comment Window is showing
        if(commentWindow!=null&&commentWindow.isShowing())
        {
            EditText edit=((EditText)commentWindow.getContentView().findViewById(R.id.comment_edittext));
            commentWindow.dismiss();
        }
     if(commentWindow==null)
     {
         commentWindow=new PopupWindow(this);
         LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
         View view=inflater.inflate(R.layout.comment_layout,null);
         commentWindow.setContentView(view);
         commentWindow.setWidth(GridLayout.LayoutParams.MATCH_PARENT);
         commentWindow.setHeight(50);
         final EditText editText=((EditText)commentWindow.getContentView().findViewById(R.id.comment_edittext));
         final int position=pos;
         editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View view, boolean hasFocus) {
                 if (hasFocus) {
                 } else {
                     String comment=editText.getText().toString();
                     if(comment.length()==0)
                         return ;
                     Bundle bundle=new Bundle();
                     bundle.putString(MyDatabase.Comment.COMMENT_COLUMN,comment);
                     bundle.putLong(MyDatabase.Comment.POST_ID,postList.get(position).getId());
                     DBManager.addComment(db_helper,bundle);
                 }
             }
         });

     }
     ((EditText)commentWindow.getContentView().findViewById(R.id.comment_edittext)).setText("");
        commentWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorWhite));
        commentWindow.setFocusable(true);
        commentWindow.showAsDropDown(button,0,20);
    }

    public void onLongClickComment(int pos)
    {
        Toast.makeText(this, "inside MAin Activity", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,CommentsActivity.class);
        intent.putExtra(MyDatabase.Comment.POST_ID,postList.get(pos).getId());
        startActivity(intent);
    }
    public void createPost()
    {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(MyDatabase.User.NAME,current_user.getName());
        startActivityForResult(intent, PostActivity.POSTACTIVITY);
    }

    private Bitmap getImage(URL url)
    {
        Bitmap bitmap=null;
        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream in=httpURLConnection.getInputStream();
            bitmap= BitmapFactory.decodeStream(in);
        }
        catch(Exception e)
        {
            bitmap=null;
        }
        return bitmap;
    }
}