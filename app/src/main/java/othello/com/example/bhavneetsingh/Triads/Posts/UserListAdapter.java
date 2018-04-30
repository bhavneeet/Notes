package othello.com.example.bhavneetsingh.Triads.Posts;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Movies.MovieWebView;
import othello.com.example.bhavneetsingh.Triads.News.*;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.User.User;
import pl.droidsonroids.gif.GifImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder>{

    interface OnClickIcon{
        void onClickIcon(View v, User user);
    }
    public interface OnClicks{
        void onClickPopupMenu(MenuItem menuItem, User user);
        void onClickLikeButton(View context,int id,User user);
        void onLongClickComment(User User);
    }
    private Activity context;
    private ArrayList<User> users;
    private UserListAdapter.OnClickIcon listener;
    private UserListAdapter.OnClicks clicksListener;
    private User current_user;
    public UserListAdapter(Activity context, ArrayList<User> users, UserListAdapter.OnClickIcon listener, UserListAdapter.OnClicks clicksListener)
    {
        this.context=context;
        this.users=users;
        this.listener=listener;
        this.clicksListener=clicksListener;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.post_list_layout,parent,false);
        UserHolder holder=new UserHolder(convertView);
        holder.textView=(TextView)convertView.findViewById(R.id.postListText);
        holder.imageView=(ImageView)convertView.findViewById(R.id.post_image);
        holder.profileImage=(ImageView)convertView.findViewById(R.id.profile_image);
        holder.date=(TextView)convertView.findViewById(R.id.post_date);
        Button but=(Button)convertView.findViewById(R.id.pop_up);
        final PopupMenu menu=new PopupMenu(context,but);
        menu.getMenuInflater().inflate(R.menu.user_popup_menu,menu.getMenu());
        holder.menu=menu;
        holder.button=but;
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        View  convertView=holder.itemView;
        final int p=holder.getAdapterPosition();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
try{

    User user =users.get(p);

    String userId=user.getUser_id();
    String user_name=user.getName();
    String category=user.getCategory();
    if(category==null)
        category="normal";
    if(category.equals("movie")||category.equals("tvshows"))
    {
        Intent intent=new Intent(context,MovieWebView.class);
        intent.putExtra(User.USER_ID,userId);
        intent.putExtra(User.NAME,user_name);
        intent.putExtra(User.CATEGORY,category);
        context.startActivity(intent);
    }
    else if(category.equals("news"))
    {
        Intent intent=new Intent(context,NewsActivity.class);
        intent.putExtra(User.USER_ID,userId);
        intent.putExtra(User.NAME,user_name);
        intent.putExtra(User.CATEGORY,category);
        context.startActivity(intent);
    }
    else
    {
        Intent intent=new Intent(context,UserPosts.class);
        intent.putExtra(User.USER_ID,userId);
        intent.putExtra(User.NAME,user_name);
        intent.putExtra(User.CATEGORY,"normal");
        context.startActivity(intent);
    }
}
catch (Exception e)
{

}
            }
        });
        final int pos=holder.getAdapterPosition();
        User user=users.get(pos);
        if(user.getProfilePictureUrl()!=null){
            Picasso.get().load(user.getProfilePictureUrl().toString()).resize(640,640).into(holder.profileImage);
        }
        if(user.getStatus_cover()!=null&&!user.getStatus_cover().equals(""))
        {
            Picasso.get().load(user.getStatus_cover().toString()).fit()
                    .into(holder.imageView);
        }
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon(v,users.get(pos));
            }
        });
        holder.textView.setText(user.getStatus_text());
        TextView textView=(TextView)convertView.findViewById(R.id.profile_name);
        textView.setText(users.get(pos).getName());
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
                    clicksListener.onClickPopupMenu(item,users.get(pos));
                    return true;
                }
            });
        }
        final ImageView icon=holder.profileImage;
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon(v,users.get(pos));
            }
        });
        final ImageButton button[]=new ImageButton[2];
        button[0]=(ImageButton)convertView.findViewById(R.id.comment_button);
        button[1]=(ImageButton) convertView.findViewById(R.id.share_button);
        final View copy=convertView;
        final GifImageView like=(GifImageView)convertView.findViewById(R.id.like_button);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicksListener.onClickLikeButton(copy,like.getId(),users.get(position));
            }
        });
        for(int i=0;i<2;i++){
            final int ind=i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicksListener.onClickLikeButton(copy,button[ind].getId(),users.get(position));
                }
            });
        }
        button[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                clicksListener.onLongClickComment(users.get(position));
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    
    class UserHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        PopupMenu menu;
        Button button;
        ImageView profileImage;
        TextView date;

        public UserHolder(View itemView) {
            super(itemView);
        }
    }
}
