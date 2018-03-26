package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.notes.Posts;
import othello.com.example.bhavneetsingh.notes.R;
import retrofit2.http.POST;

public class MovieAdapter extends PagerAdapter {
    Activity context;
    ArrayList<Posts>newsList;

    public MovieAdapter(Activity context, ArrayList<Posts> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.news_list_layout,collection,false);
        NewsHolder holder=new NewsHolder(convertView);
        int pos=position;
        Posts news=newsList.get(pos);
        Picasso.get().load(news.getImgUrl()).fit().into(holder.poster);
        holder.title.setText(news.getName());
        holder.content.setText(news.getContent());
        holder.nextNews.setText("No more news");
        if(pos<newsList.size()-1)
            holder.nextNews.setText(newsList.get(pos+1).getName());
        collection.addView(convertView);
        return  convertView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    class NewsHolder{
        ImageView poster;
        TextView title;
        TextView content;
        TextView nextNews;
        public NewsHolder(View itemView) {
            poster=(ImageView)itemView.findViewById(R.id.news_image);
            title=(TextView)itemView.findViewById(R.id.news_title);
            content=(TextView)itemView.findViewById(R.id.news_content);
            nextNews=(TextView)itemView.findViewById(R.id.next_news);
        }
    }
}
