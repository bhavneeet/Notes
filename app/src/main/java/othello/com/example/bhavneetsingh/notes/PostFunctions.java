package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

import static othello.com.example.bhavneetsingh.notes.MainActivity.EDITPOST;

public class PostFunctions implements PostListAdapter.OnClickIcon,PostListAdapter.OnClicks{
    Activity context;
    private PopupWindow profilePic;
    private final HashMap<String,Integer> followers=new HashMap<>();
    RecyclerView recyclerView;
    public PostFunctions(Activity context,RecyclerView recyclerView) {
        this.context = context;
        profilePic=new PopupWindow(context);
        this.recyclerView=recyclerView;
    }
    private View convertView;

    public void onClickIcon(View view, MyDatabase.User user) {

        {
            if (convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.icon_view,recyclerView,false);
                //Setting Proflie Image
                profilePic.setContentView(convertView);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                profilePic.setEnterTransition(new Slide());
                profilePic.setExitTransition(new Slide());
            }        /*View v=(convertView.findViewById(R.id.follow));
                        *  *//*if(!current_user.getUser_id().equals(user.getUser_id()))
            {
                if(followers.containsKey(user.getUser_id()))
                {
                    v.setAlpha((float)1);
                }
                else {
                    v.setAlpha((float) 0.5);
                }
            }
            else
            {
                v.setAlpha((float)1);
                v.setClickable(false);
            }*//*
            if(current_user.getUser_id().equals(user.getUser_id()))
            {
                v.setAlpha((float)1);
                v.setClickable(false);
            }*/
            ImageView edit=(ImageView)convertView.findViewById(R.id.edit_profilepic);
            ImageView imageView=(ImageView)convertView.findViewById(R.id.icon_image);
            if(user.getProfilePictureUrl()!=null)
                Picasso.get().load(user.getProfilePictureUrl().toString()).into(imageView);
            ((TextView)convertView.findViewById(R.id.icon_title)).setText(user.getName());
            final MyDatabase.User copy=user;
            final String user_id =user.getUser_id();
      /*      ((ImageView)convertView.findViewById(R.id.follow)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(followers.containsKey(user_id))
                        follow(v,copy);
                    else
                        follow(v,copy);
                }
            });
      */      profilePic.setElevation(20);
            profilePic.setFocusable(true);
            View root=context.findViewById(R.id.content_home);
            profilePic.showAtLocation(root, Gravity.CENTER,0,0);
        }
    }
    public void onClickPopupMenu(MenuItem menuItem, Posts posts) {
        /*if(!postList.get(pos).getUser().getUser_id().equals(current_user.getUser_id()))
        {
            Toast.makeText(this, "Not Your Property", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(menuItem.getItemId() == R.id.editMenuItem) {

/*
          Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra(MyDatabase.User.NAME,posts.getUser().getName());
            String text = posts.getContent();
            intent.putExtra(Posts.KEY_CONTEXT, text);
            intent.putExtra("edit",true);
            String img="null";
            if(posts.getImgUrl()!=null)
                img=posts.getImgUrl().toString();
            intent.putExtra(Posts.KEY_SMILEY,posts.getSmiley_id());
            intent.putExtra(Posts.IMAGE,img);
            intent.putExtra(MyDatabase.User.PROFILE_PICTURE,posts.getUser().getProfilePictureUrl().toString());
            intent.putExtra(Posts.KEY_ID,posts.getId());
            context.startActivityForResult(intent, EDITPOST);*/
        }
        else if(menuItem.getItemId()==R.id.deleteMenuItem)
        {
/*            if(postList.size()>0){
                Posts posts=postList.get(pos);
                DBManager.delete(posts.getId(), new OnDownloadComplete<Posts>() {
                    @Override
                    public void onDownloadComplete(Posts result) {
                    }
                });
                postList.remove(pos);
                adapter.notifyDataSetChanged();
            }*/
        }
        else if(menuItem.getItemId()==R.id.reportMenuItem)
        {/*
            Toast.makeText(this,"Reported",Toast.LENGTH_LONG).show();*/
        }
    }public void onClickLikeButton(View context,int id, Posts posts) {
        if (window != null) {
            if (!window.isShowing()) {
            }
        }
        if (id == R.id.like_button) {
            GifImageView but=(GifImageView)(context.findViewById(id));
            onLikeClick(context,but,posts);
        }
   /*     else if (id == R.id.comment_button) {
            ImageButton button = (ImageButton) (context.findViewById(id));
            onCommentClick(context,button,pos);
        }
        else if (id== R.id.share_button) {
            sharePost(context,pos);*//*

        }
    }*/
    }
    //Shsre current post
    public void sharePost(View context,int pos)
    {
/*        Intent intent=new Intent();
        MyDatabase.User user=postList.get(pos).getUser();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        context.startActivity(Intent.createChooser(intent,text));*/
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    private boolean likeClicked = false;
    private PopupWindow window;
    static final HashMap<Integer,Integer>map=new HashMap<>();

    static final HashMap<String,Integer>smi=new HashMap<>();

    public static int getSmiley(String smiley_id)
    {
        map.put(R.id.like_options_happy, R.drawable.like_icon);
        map.put(R.id.like_options_angry, R.drawable.angry);
        map.put(R.id.like_options_sad, R.drawable.sad);
        map.put(R.id.like_options_thuglife, R.drawable.happy);
        smi.put("like",R.drawable.like_icon);
        smi.put("laugh", R.drawable.laugh);
        smi.put("angry", R.drawable.angry);
        smi.put("sad", R.drawable.sad);
        smi.put("thuglife", R.drawable.happy);
        int id=R.drawable.like_icon;
        if(smi.containsKey(smiley_id))
            id=smi.get(smiley_id);
        return id;
    }
    //On clicking of like button
    public void onLikeClick(View context, final GifImageView button, final Posts posts) {
        int winHeight=0;
        if (window == null) {
            window = new PopupWindow(this.context);
            LayoutInflater inflater = (LayoutInflater) (this.context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.like_options, null);
            winHeight=view.getHeight();
            window.setContentView(view);
        }
        int height = button.getHeight();
        window.setBackgroundDrawable((this.context).getResources().getDrawable(R.color.colorWhite));
        View view = window.getContentView();

        for (Integer key : map.keySet()) {
            final int k = key;
            view.findViewById(key).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setImageResource(map.get(k));
                    switch(k){
                        case R.id.like_options_happy:
                            posts.setSmiley_id("laugh");
                        case R.id.like_options_angry:
                            posts.setSmiley_id("angry");
                        case R.id.like_options_sad:
                            posts.setSmiley_id("sad");
                        case R.id.like_options_thuglife:
                             posts.setSmiley_id("thuglife");
                    }
                    DBManager.editPost(posts, new OnDownloadComplete<Posts>() {
                        @Override
                        public void onDownloadComplete(Posts result) {
                        posts.setSmiley_id(result.getSmiley_id());
                        }
                    });
                    window.dismiss();
                }
            });
        }
        window.setFocusable(true);
        int offy = (int) dpToPx(50)+height;
        window.showAsDropDown(button, 40, -(offy));
        likeClicked = true;
    }
    private PopupWindow commentWindow=null;
    public void onCommentClick(View context,ImageButton button,int pos)
    {

        /*//When Comment Window is showing
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
        commentWindow.showAsDropDown(button,0,20);*/
    }

    public void onLongClickComment(Posts posts)
    {
/*        Toast.makeText(this, "inside MAin Activity", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,CommentsActivity.class);
        intent.putExtra(MyDatabase.Comment.POST_ID,postList.get(pos).getId());
        startActivity(intent);*/
    }
}
