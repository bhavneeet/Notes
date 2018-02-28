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
            holder.button=(Button)convertView.findViewById(R.id.follow_button);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ProfileHolder)convertView.getTag();
        }
        holder.textView.setText(iconArrayList.get(position).getName());
        final Button button=holder.button;
        {
            MyDatabase db_helper=MyDatabase.getInstance(context);
            boolean check=DBManager.checkConnection(db_helper,current.getUser_id(),iconArrayList.get(position).getUser_id());
            button.setTag(check);
            if(check)
                button.setText("Unfollow");
            else
                button.setText("Follow");
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickFollowButton(button,position);
            }
        });
        return convertView;
    }
    public interface FollowListener{
        public void onClickFollowButton(Button button,int pos);
    }
    class ProfileHolder{
    CircleImageView imageView;
    TextView textView;
    Button button;
    }
}
