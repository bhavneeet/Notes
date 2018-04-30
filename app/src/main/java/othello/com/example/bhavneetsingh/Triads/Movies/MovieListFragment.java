package othello.com.example.bhavneetsingh.Triads.Movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.Transitions.CubeInDepthTransformation;
import othello.com.example.bhavneetsingh.Triads.User.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment {
    private User user;
    private ArrayList<Movie>movies;
    private MovieAdapter movieAdapter;
    private ViewPager viewPager;

    public ViewPager getViewPager() {

        return viewPager;
    }

    public MovieAdapter getMovieAdapter() {
        return movieAdapter;
    }

    private OnFragmentInteractionListener mListener;
    private int current_position=-1;
    MovieAdapter.MovieSelectedListener movieSelectedListener;
    @SuppressLint("ValidFragment")
    private MovieListFragment() {
        // Required empty public constructor
    }
    public static MovieListFragment newInstance(User user, MovieAdapter.MovieSelectedListener movieSelectedListener) {
        MovieListFragment fragment = new MovieListFragment();
        fragment.movieSelectedListener=movieSelectedListener;
        fragment.user=user;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        movies=new ArrayList<>();
        movieAdapter=new MovieAdapter(getActivity(),movies);
        movieAdapter.setMovieSelectedListener(movieSelectedListener);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager=view.findViewById(R.id.movies_list);
        viewPager.setPageTransformer(true,new CubeInDepthTransformation());
        viewPager.setAdapter(movieAdapter);
        refresh();
    }

    public Movie getCurrentMovie()
    {
        current_position=viewPager.getCurrentItem();
        return movies.get(current_position);
    }
    public void refresh()
    {
        DBManager.fetchMovies(user, new OnDownloadComplete<ArrayList<Movie>>() {
            @Override
            public void onDownloadComplete(ArrayList<Movie> result) {
                if(result!=null)
                {
                    movies.clear();
                    movies.addAll(result);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }
/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
