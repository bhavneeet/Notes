
package othello.com.example.bhavneetsingh.notes;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class PostListAdapter extends  RecyclerView.Adapter<PostListAdapter.RPostHolder> {

    interface OnClickIcon{
        void onClickIcon(View v, MyDatabase.User user);
    }
    private Activity context;
    private ArrayList<Posts> posts;
    private OnClickIcon listener;
    private OnClicks clicksListener;
    private MyDatabase.User current_user;
    public PostListAdapter(Activity context, ArrayList<Posts> posts, PostListAdapter.OnClickIcon listener, PostListAdapter.OnClicks clicksListener)
    {
        this.context=context;
        this.posts=posts;
        this.listener=listener;
        this.clicksListener=clicksListener;
    }

    public ArrayList<Posts> getPosts() {
        return posts;
    }

    @Override
    public PostListAdapter.RPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.post_list_layout,parent,false);
        PostListAdapter.RPostHolder holder=new PostListAdapter.RPostHolder(convertView);
        holder.textView=(TextView)convertView.findViewById(R.id.postListText);
        holder.imageView=(ImageView)convertView.findViewById(R.id.post_image);
        holder.profileImage=(ImageView)convertView.findViewById(R.id.profile_image);
        holder.date=(TextView)convertView.findViewById(R.id.post_date);
        Button but=(Button)convertView.findViewById(R.id.pop_up_button);
        final PopupMenu menu=new PopupMenu(context,but);
        menu.getMenuInflater().inflate(R.menu.pop_up_menu,menu.getMenu());
        holder.menu=menu;
        holder.button=but;
        return holder;
    }

    @Override
    public void onBindViewHolder(PostListAdapter.RPostHolder holder, final int position) {
        View  convertView=holder.itemView;
        final int p=holder.getAdapterPosition();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posts.get(p).getUser().getCategory()=="movie")
                {/*
                    String text=posts.get(p).getContent();
                    String image=posts.get(p).getImgUrl();
                    Bundle bundle=new Bundle();
                    bundle.putString(MovieAdapter.Movie.TITLE,text);
                    bundle.putString(MovieAdapter.Movie.IMAGE,image);
                    Intent intent=new Intent(context,MoviesActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
                }
                else
                {
                    Posts post=posts.get(p);
                    String userId=post.getUser_id();
                    String user_name=post.getName();
                    String category=post.getCategory();
                    Intent intent=new Intent(context,NewsActivity.class);
                    intent.putExtra(MyDatabase.User.USER_ID,userId);
                    intent.putExtra(MyDatabase.User.NAME,user_name);
                    intent.putExtra(MyDatabase.User.CATEGORY,category);
                    context.startActivity(intent);
                }
            }
        });
        final int pos=holder.getAdapterPosition();
        if(posts.get(pos).getUser().getProfilePictureUrl()!=null){
            Picasso.get().load(posts.get(pos).getUser().getProfilePictureUrl().toString()).resize(640,640).into(holder.profileImage);
        }
        if(posts.get(pos).getImgUrl()!=null)
        {
            Picasso.get().load(posts.get(pos).getImgUrl().toString()).fit()
                    .into(holder.imageView);
        }
         holder.profileImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.onClickIcon(v,posts.get(pos).getUser());
             }
         });
        holder.textView.setText(posts.get(pos).getContent());
        TextView textView=(TextView)convertView.findViewById(R.id.profile_name);
        textView.setText(posts.get(pos).getUser().getName());
        holder.date.setText(posts.get(pos).getDate_edited().substring(0,10));
        final PopupMenu menu=holder.menu;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        });
        if(clicksListener!=null)
        {
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    clicksListener.onClickPopupMenu(item,posts.get(position));
                    return true;
                }
            });
        }
        final ImageView icon=holder.profileImage;
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon(v,posts.get(position).getUser());
            }
        });
        final ImageButton button[]=new ImageButton[2];
        button[0]=(ImageButton)convertView.findViewById(R.id.comment_button);
        button[1]=(ImageButton) convertView.findViewById(R.id.share_button);
        final View copy=convertView;
        final GifImageView like=(GifImageView)convertView.findViewById(R.id.like_button);
        like.setImageResource(PostFunctions.getSmiley(posts.get(pos).getSmiley_id()));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicksListener.onClickLikeButton(copy,like.getId(),posts.get(position));
            }
        });
        for(int i=0;i<2;i++){
            final int ind=i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicksListener.onClickLikeButton(copy,button[ind].getId(),posts.get(position));
                }
            });
        }
        button[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                clicksListener.onLongClickComment(posts.get(position));
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnClicks{
        void onClickPopupMenu(MenuItem menuItem,Posts posts);
        void onClickLikeButton(View context,int id,Posts posts);
        void onLongClickComment(Posts posts);
    }
    class RPostHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        PopupMenu menu;
        Button button;
        ImageView profileImage;
        TextView date;
        public RPostHolder(View itemView) {
            super(itemView);
        }

    }

    public void setOnClicks(OnClicks menuListener)
    {
        clicksListener=menuListener;
    }

    /*@Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return posts.get(position).getId();
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater=context.getLayoutInflater();
        PostHolder holder=new PostHolder();
        if(convertView==null)
        {
           convertView=inflater.inflate(R.layout.post_list_layout,parent,false);
           holder.textView=(TextView)convertView.findViewById(R.id.postListText);
           holder.imageView=(ImageView)convertView.findViewById(R.id.post_image);
           holder.profileImage=(ImageView)convertView.findViewById(R.id.profile_image);
           Button but=(Button)convertView.findViewById(R.id.pop_up_button);
           final PopupMenu menu=new PopupMenu(context,but);
           menu.getMenuInflater().inflate(R.menu.pop_up_menu,menu.getMenu());
           holder.menu=menu;
           holder.button=but;
           convertView.setTag(holder);
        }
        else
        {
            holder=(PostHolder)convertView.getTag();

        }
        if(posts.get(pos).getUser().getProfilePictureUrl()!=null){
            Log.d("picasso",posts.get(pos).getImgUrl().toString());
            Picasso.get().load(posts.get(pos).getUser().getProfilePictureUrl().toString()).into(holder.profileImage);
            Picasso.get().load(posts.get(pos).getImgUrl().toString()).into(holder.imageView);
        }

        holder.textView.setText(posts.get(pos).getContent());
        TextView textView=(TextView)convertView.findViewById(R.id.profile_name);
        textView.setText(posts.get(pos).getUser().getName());
        final PopupMenu menu=holder.menu;
        final int position=pos;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        });
        if(clicksListener!=null)
        {
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    clicksListener.onClickPopupMenu(item,position);
                     return true;
                }
            });
        }
        final ImageView icon=holder.profileImage;
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon(v,posts.get(position).getUser());
            }
        });
        final ImageButton button[]=new ImageButton[2];
        button[0]=(ImageButton)convertView.findViewById(R.id.comment_button);
        button[1]=(ImageButton) convertView.findViewById(R.id.share_button);
        final View copy=convertView;
        final GifImageView like=(GifImageView)convertView.findViewById(R.id.like_button);
        like.setImageResource(posts.get(pos).getSmiley_id());
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicksListener.onClickLikeButton(copy,like.getId(),position);
            }
        });
        for(int i=0;i<2;i++){
              final int ind=i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicksListener.onClickLikeButton(copy,button[ind].getId(),position);
                }
            });
        }
        button[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                clicksListener.onLongClickComment(position);
                return true;
            }
        });
        return convertView;
    }*/
}
class PostHolder{
    TextView textView;
    ImageView imageView;
    PopupMenu menu;
    Button button;
    ImageView profileImage;
}