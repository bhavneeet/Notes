
package othello.com.example.bhavneetsingh.notes;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class PostListAdapter extends BaseAdapter {
        public interface OnClickIcon{
            void onClickIcon();
        }
    private Activity context;
    private ArrayList<Posts> posts;
    private OnClickIcon listener;
    private OnClicks clicksListener;
    PostListAdapter(Activity context, ArrayList<Posts> posts, OnClickIcon listener)
    {
        this.context=context;
         this.posts=posts;
        this.listener=listener;

    }

    public void setOnClicks(OnClicks menuListener)
    {
        clicksListener=menuListener;
    }

    @Override
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

    public View getView(final int pos, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        PostHolder holder=new PostHolder();
        Log.d("Positon",pos+""+posts.get(pos).getUser().getUser_id());
        if(convertView==null)
        {
           convertView=inflater.inflate(R.layout.post_list_layout,parent,false);
           holder.textView=(TextView)convertView.findViewById(R.id.postListText);
           Button but=(Button)convertView.findViewById(R.id.pop_up_button);
           final PopupMenu menu=new PopupMenu(context,but);
           menu.getMenuInflater().inflate(R.menu.pop_up_menu,menu.getMenu());
           holder.menu=menu;
           but.setOnClickListener(new View.OnClickListener() {
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
                       clicksListener.onClickPopupMenu(item,pos);
                       return true;
                   }
               });
           }
           convertView.setTag(holder);
        }
        else
        {
            holder=(PostHolder)convertView.getTag();
        }
        holder.textView.setText(posts.get(pos).getContent());
        TextView textView=(TextView)convertView.findViewById(R.id.profile_name);
        textView.setText(posts.get(pos).getUser().getName());
        final ImageView icon=(ImageView)convertView.findViewById(R.id.profile_image);
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon();
            }
        });
        final ImageButton button[]=new ImageButton[2];
        button[0]=(ImageButton)convertView.findViewById(R.id.comment_button);
        button[1]=(ImageButton) convertView.findViewById(R.id.share_button);
        final View copy=convertView;
        final int position=pos;
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
    }
     public interface OnClicks{
     void onClickPopupMenu(MenuItem menuItem,int pos);
     void onClickLikeButton(View context,int id,int pos);
     void onLongClickComment(int pos);
    }
}
class PostHolder{
    TextView textView;
    ImageView imageView;
    PopupMenu menu;
}