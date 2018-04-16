package othello.com.example.bhavneetsingh.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment {
    private MyDatabase.User user;
    private ArrayList<Movie>movies;
    private MovieAdapter movieAdapter;
    private ViewPager viewPager;
    private OnFragmentInteractionListener mListener;

    @SuppressLint("ValidFragment")
    private MovieListFragment() {
        // Required empty public constructor
    }
    public static MovieListFragment newInstance(MyDatabase.User user) {
        MovieListFragment fragment = new MovieListFragment();
        fragment.user=user;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies=new ArrayList<>();
        movieAdapter=new MovieAdapter(getActivity(),movies);

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
