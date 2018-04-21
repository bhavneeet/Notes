package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.notes.Posts;
import othello.com.example.bhavneetsingh.notes.R;
import retrofit2.http.POST;

public class MovieAdapter extends PagerAdapter {
    Activity context;
    ArrayList<Movie>moviesList;
    MovieSelectedListener movieSelectedListener;
    private int last_page=0;
    private int count=1;
    public MovieSelectedListener getMovieSelectedListener() {
        return movieSelectedListener;
    }
    public interface OnPageEndListener{
      void  onPageEnd(int last_page,ArrayList<Movie>movies);
    }
    public void setMovieSelectedListener(MovieSelectedListener movieSelectedListener) {
        this.movieSelectedListener = movieSelectedListener;
    }

    interface MovieSelectedListener{
        void onSelectedMovie(int postion,Movie movie);
    }
    private OnPageEndListener onPageEndListener;

    public OnPageEndListener getOnPageEndListener() {
        return onPageEndListener;
    }

    public void setOnPageEndListener(OnPageEndListener onPageEndListener) {
        this.onPageEndListener = onPageEndListener;
    }

    public MovieAdapter(Activity context, ArrayList<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }
    public static final String HOST="https://api.themoviedb.org/3/tv/",API="?api_key=25f4781cc8f62871f85f54edffe19be0&language=en-US",LINK="http://triads.herokuapp.com/imdb/tvshows/seasons";
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater=context.getLayoutInflater();
        final View convertView=inflater.inflate(R.layout.content_movie,collection,false);

        final MovieHolder holder=new MovieHolder(convertView);
        final int pos=position;
        final Movie news=moviesList.get(pos);
        Picasso.get().load(news.getPoster()).fit().into(holder.poster);
        holder.title.setText(news.getName());
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieSelectedListener!=null)
                {
                    movieSelectedListener.onSelectedMovie(pos,moviesList.get(pos));
                }
            }
        });
        Log.d("page",pos+"=="+last_page);
        if(pos==moviesList.size()-1&&pos!=last_page)
        {
            if(onPageEndListener!=null)
            {
                last_page=pos;
                onPageEndListener.onPageEnd(++count,moviesList);
            }
        }
        collection.addView(convertView);
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
        View itemView;

        public View getItemView() {
            return itemView;
        }

        public void setItemView(View itemView) {
            this.itemView = itemView;
        }

        /*      TextView content;
                Button button;
                FloatingActionButton play;*/
/*        RecyclerView season_view,cast_view;
        ShowDetailAdapter season_adapter,cast_adapter;
        ArrayList<ShowDetail>season_list,cast_list;*/
        android.support.v7.widget.Toolbar toolbar;
        public MovieHolder(View itemView) {
            setItemView(itemView);
            poster=(ImageView)itemView.findViewById(R.id.movie_poster);
            title=(TextView)itemView.findViewById(R.id.movie_description_title);
/*            content=(TextView)itemView.findViewById(R.id.movie_description);
            button=(Button)itemView.findViewById(R.id.load_more);*/
/*            toolbar=itemView.findViewById(R.id.movie_toolbar);*/
            //init
            /*play=itemView.findViewById(R.id.fab);
            *//*LinearLayout season=itemView.findViewById(R.id.seasons_list),cast=itemView.findViewById(R.id.cast_list);
            ((TextView)season.findViewById(R.id.item_heading)).setText("Seasons");
            ((TextView)cast.findViewById(R.id.item_heading)).setText("Casts");
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
            cast_view.setLayoutManager(cast_manager);*/
            //season title
        }
    }
}
