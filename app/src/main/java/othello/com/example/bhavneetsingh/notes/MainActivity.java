package othello.com.example.bhavneetsingh.notes;

import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements PostListAdapter.OnClickIcon,PostListAdapter.OnClicks,View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static int id = 3;
    public static final int MAINACTIVITY = 1;
    private final ArrayList<Posts> postList= new ArrayList<>();;
    private PostListAdapter adapter;
    private ListView list;
    private EditText editText;
    private android.app.AlertDialog.Builder builder;//for Profile Photo
    private MyDatabase db_helper;//My Databse
    private Bundle user_bundle;
    private MyDatabase.User current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db_helper =  MyDatabase.getInstance(this);
        Intent intent=getIntent();
        //Getting user profile from login activity
        if(intent!=null)
        {
            user_bundle=intent.getExtras();
            current_user=DBManager.containsUser(db_helper,user_bundle.getString(MyDatabase.User.USER_ID));
        }
        TextView header_name=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_header);
        header_name.setText(current_user.getName());
        TextView header_id=(TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_id);
        header_id.setText(current_user.getUser_id());
        //For Welcoming
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        editText = (EditText) findViewById(R.id.postText);
        adapter = new PostListAdapter(this, postList, this);
        adapter.setOnClicks(this);
        list = (ListView) findViewById(R.id.allPosts);
        list.setAdapter(adapter);
        list.setClickable(true);
        refresh();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(this);
        builder=new android.app.AlertDialog.Builder(this);
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void fetchMyNotes()
    {

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
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh)
        {
            refresh();
        }
        else if(item.getItemId()==R.id.sugestions)
        {
            showFollowPopup();
        }
        return true;
    }
    //Refreshing
    private void refresh()
    {
        postList.clear();
        Log.d("size",""+postList.size());
        ArrayList<Posts> list=DBManager.fetchList(db_helper,current_user);
        for(Posts posts:list)
            postList.add(posts);
        adapter.notifyDataSetChanged();
    }
/*
* When Post is created then result from post activity is added to list in main activity*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PostActivity.POSTACTIVITY) {
            if (resultCode == MAINACTIVITY) {
                if (data != null) {

                    String text = data.getStringExtra(MyDatabase.KEY_CONTEXT);
                    int smileyId = data.getIntExtra(MyDatabase.KEY_SMILEY, R.drawable.smiley);
                    Bundle bundle = new Bundle();
                    bundle.putString(MyDatabase.KEY_CONTEXT, text);
                    bundle.putInt(MyDatabase.KEY_SMILEY, smileyId);
                    Posts posts = null;
                    //Storing new Post in Databse
                    posts = DBManager.add(db_helper, bundle,current_user);
                    postList.add(0,posts);
                    adapter.notifyDataSetChanged();
                }
            }
            else if (resultCode == EDITPOST)
            {
                if (data != null) {

                    String text = data.getStringExtra(MyDatabase.KEY_CONTEXT);
                    int smileyId = data.getIntExtra(MyDatabase.KEY_SMILEY, R.drawable.smiley);
                    int position = data.getIntExtra("position", -1);
                    Bundle bundle = new Bundle();
                    if (position >= 0) {
                        postList.get(position).setContent(text);
                        long id = postList.get(position).getId();
                        Posts posts = null;
                        //Storing existing Post in Databse
                        DBManager.update(db_helper, postList.get(position));
                        postList.get(position).setContent(text);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public static final int EDITPOST=10;
    //When profile photo is clicked
    private View convertView;
    private android.app.AlertDialog dialog = null;

    @Override
    public void onClickIcon() {

        {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.icon_view, null);
                ((ImageView) convertView.findViewById(R.id.icon_image)).setImageResource(R.drawable.icon2);
                builder.setView(convertView);
                dialog = builder.create();
                dialog.show();
            } else {
                dialog.show();
            }
            dialog.getWindow().setLayout(1000, 1000);

        }
    }
    //When menuitem of posts is clicked

    public void onClickPopupMenu(MenuItem menuItem, int pos) {
        Log.d("hellomam",postList.get(pos).getUser().getUser_id()+" "+postList.get(pos).getUser().getName());
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
//                if(postList.size()>0){
//                    Posts posts=postList.get(pos);
//                    Log.d("error",pos+"");
//                    DBManager.delete(db_helper,posts.getId());
//                    postList.remove(pos);
//                    adapter.notifyDataSetChanged();
           // }
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
        else if (id == R.id.share_button) {
           Toast.makeText(this,"Can't share now",Toast.LENGTH_LONG).show();

        }
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
                    DBManager.update(db_helper,postList.get(pos));
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
   public void createPost()
   {
       Intent intent = new Intent(this, PostActivity.class);
       intent.putExtra(MyDatabase.User.NAME,current_user.getName());
       startActivityForResult(intent, PostActivity.POSTACTIVITY);

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


}