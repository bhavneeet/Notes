package othello.com.example.bhavneetsingh.notes;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bhavneet singh on 20-Feb-18.
 */

public class ProfileAdapter extends BaseAdapter {
    private ArrayList<MyDatabase.User>iconArrayList;
    private Context context;
    private FollowListener listener;
    MyDatabase.User current;
    public ProfileAdapter(Context context, ArrayList<MyDatabase.User>icons, FollowListener listener, MyDatabase.User current)
    {
        this.context=context;
        this.iconArrayList=icons;
       this.listener=listener;
        this.current=current;
    }
    @Override
    public int getCount() {
        return iconArrayList.size();
    }

    @Override
    public MyDatabase.User getItem(int position) {
        return iconArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.textView.setText(iconArrayList.get(position).getName());
        Picasso.get().load(iconArrayList.get(position).getProfilePictureUrl()).into(holder.imageView);
        holder.followme.setClickable(true);
        holder.followme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickFollowButton(v,current,iconArrayList.get(position));
            }
        });
        return convertView;
    }
    public interface FollowListener{
         void onClickFollowButton(View v, MyDatabase.User current_user, MyDatabase.User follower);
    }
    class ProfileHolder{
    CircleImageView imageView;
    TextView textView;
    CircleImageView followme;

    }
}
