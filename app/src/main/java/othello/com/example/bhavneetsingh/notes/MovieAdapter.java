package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.notes.Posts;
import othello.com.example.bhavneetsingh.notes.R;
import retrofit2.http.POST;

public class MovieAdapter extends PagerAdapter {
    Activity context;
    ArrayList<Movie>moviesList;

    public MovieAdapter(Activity context, ArrayList<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }
    public static final String HOST="https://api.themoviedb.org/3/tv/",API="?api_key=25f4781cc8f62871f85f54edffe19be0&language=en-US",LINK="http://triads.herokuapp.com/imdb/tvshows/seasons";
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.movies_list,collection,false);
        final MovieHolder holder=new MovieHolder(convertView);
        int pos=position;
        Movie news=moviesList.get(pos);
        Picasso.get().load(news.getPoster()).fit().into(holder.poster);
        holder.title.setText(news.getName());
        holder.content.setText(news.getDescription());
        holder.button.setText("Load more");
        holder.button.setBackground(null);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.button.getText().equals("Load more"))
                {
                    holder.button.setText("Load less");
                    holder.content.setMaxLines(1000);
                }
                else
                {
                    holder.button.setText("Load more");
                    holder.content.setMaxLines(3);
                }
            }
        });
        collection.addView(convertView);

        DBManager.fetchSeasons(news.getId(), new OnDownloadComplete<ArrayList<Season>>() {
            @Override
            public void onDownloadComplete(ArrayList<Season> result) {
             if(result!=null)
             {
                 holder.season_list.addAll(result);
                 holder.season_adapter.notifyDataSetChanged();
             }
            }
        });
        return  convertView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    class MovieHolder{
        ImageView poster;
        TextView title;
        TextView content;
        Button button;
        RecyclerView season_view,cast_view;
        ShowDetailAdapter season_adapter,cast_adapter;
        ArrayList<ShowDetail>season_list,cast_list;
        android.support.v7.widget.Toolbar toolbar;
        public MovieHolder(View itemView) {
            poster=(ImageView)itemView.findViewById(R.id.movie_poster);
            title=(TextView)itemView.findViewById(R.id.movie_description_title);
            content=(TextView)itemView.findViewById(R.id.movie_description);
            button=(Button)itemView.findViewById(R.id.load_more);
/*            toolbar=itemView.findViewById(R.id.movie_toolbar);*/
 //init
            LinearLayout season=itemView.findViewById(R.id.seasons_list),cast=itemView.findViewById(R.id.cast_list);
            season_list=new ArrayList<>();
            cast_list=new ArrayList<>();
            season_view=season.findViewById(R.id.items_list);
            cast_view=cast.findViewById(R.id.items_list);
            season_adapter=new ShowDetailAdapter(itemView.getContext(),season_list);
            cast_adapter=new ShowDetailAdapter(itemView.getContext(),cast_list);
            season_view.setAdapter(season_adapter);
            LinearLayoutManager season_manager=new LinearLayoutManager(itemView.getContext());
            season_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            season_view.setLayoutManager(season_manager);
            cast_view.setAdapter(cast_adapter);
            LinearLayoutManager cast_manager=new LinearLayoutManager(itemView.getContext());
            cast_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            cast_view.setLayoutManager(cast_manager);
            //season title
        }
    }
}
