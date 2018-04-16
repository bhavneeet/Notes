package othello.com.example.bhavneetsingh.notes;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
public class ProfileAdapter extends BaseAdapter {
    private ArrayList<MyDatabase.User> users;
    private Context context;
    private FollowListener listener;
    MyDatabase.User current;
    OnItemClick itemClick;
    HashMap<String,Boolean>followers;
    public OnItemClick getItemClick() {
        return itemClick;
    }
    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    interface OnItemClick{
        void onClick(MyDatabase.User user);
    }
    public ProfileAdapter(Context context, ArrayList<MyDatabase.User>icons, FollowListener listener, MyDatabase.User current)
    {
        this.context=context;
        this.users =icons;
       this.listener=listener;
        this.current=current;
        followers=new HashMap<>();
        }

    public ProfileAdapter(Context context, ArrayList<MyDatabase.User> users, FollowListener listener, MyDatabase.User current, HashMap<String, Boolean> followers) {
        this.users = users;
        this.context = context;
        this.listener = listener;
        this.current = current;
        this.itemClick = itemClick;
        this.followers = followers;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public MyDatabase.User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int pos, View convertView, ViewGroup parent) {
        final int position=pos;
        ProfileHolder holder=new ProfileHolder();
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.share_follow_list,parent,false);
            holder.imageView=(CircleImageView)convertView.findViewById(R.id.circleImageView);
            holder.textView=(TextView)convertView.findViewById(R.id.textView10);
            holder.followme=(CircleImageView)convertView.findViewById(R.id.followme);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ProfileHolder)convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)
                {
                    itemClick.onClick(users.get(position));
                }
            }
        });
        if(followers.containsKey(users.get(position).getUser_id()))
        {
            holder.followme.setAlpha((float)(0.3));
        }
        holder.textView.setText(users.get(position).getName());
        Picasso.get().load(users.get(position).getProfilePictureUrl()).into(holder.imageView);
        holder.followme.setClickable(true);
        holder.followme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                {
                    listener.onClickFollowButton(v,current.getUser_id(), users.get(position).getUser_id());
                }
            }
        });
        return convertView;
    }
    public interface FollowListener{
         void onClickFollowButton(View v, String current_user, String follower);
    }
    class ProfileHolder{
    CircleImageView imageView;
    TextView textView;
    CircleImageView followme;

    }
}
