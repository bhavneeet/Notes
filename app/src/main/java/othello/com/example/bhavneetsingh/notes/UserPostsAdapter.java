package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class UserPostsAdapter extends PagerAdapter {
    Activity context;
    ArrayList<Posts> postsList;
    onPostClickListener postClickListener;
    public UserPostsAdapter(Activity context, ArrayList<Posts> postsList,onPostClickListener postClickListener) {
        this.context = context;
        this.postsList=postsList;
        this.postClickListener=postClickListener;
    }
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.user_posts_list,collection,false);
        final int pos=position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener.onClick(postsList.get(pos));
            }
        });
        UserPostsHolder postsHolder=new UserPostsHolder(convertView);
        Posts posts=postsList.get(position);
        postsHolder.profile_name.setText(posts.getUser().getName());
        Picasso.get().load(posts.getUser().getProfilePictureUrl()).fit().into(postsHolder.profile_image);
        postsHolder.content.setText(posts.getContent());
        Picasso.get().load(posts.getImgUrl()).fit().into(postsHolder.post_image);
        collection.addView(convertView);
        return  convertView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @Override
    public int getCount() {
        return postsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    class UserPostsHolder{
        CircleImageView profile_image;
        TextView profile_name;
        TextView date;
        TextView content;
        GifImageView post_image;
        public UserPostsHolder(View itemView) {
            profile_image=itemView.findViewById(R.id.profile_image);
            profile_name=itemView.findViewById(R.id.profile_name);
            date=itemView.findViewById(R.id.post_date);
            content=itemView.findViewById(R.id.post_text);
            post_image=itemView.findViewById(R.id.post_image);
        }
    }
    interface onPostClickListener{
        void onClick(Posts posts);
    }
}