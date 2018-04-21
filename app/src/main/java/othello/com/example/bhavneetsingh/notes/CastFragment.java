package othello.com.example.bhavneetsingh.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CastFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private String id;
    private RecyclerView seasonView;
    private ShowDetailAdapter castsAdapter;
    private ArrayList<ShowDetail> casts;
    private View view;
    @SuppressLint("ValidFragment")
    private CastFragment() {
        // Required empty public constructor
    }
    public String getMovieId() {
        return id;
    }

    public void setMovieId(String id) {
        this.id = id;
    }


    public static CastFragment newInstance(String id) {
        CastFragment fragment = new CastFragment();
        Bundle bundle=new Bundle();
        bundle.putString(MovieDetailActivity.ID,id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_season, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view= view;
        seasonView=view.findViewById(R.id.seasons_list);
        seasonView.setLayoutManager(new GridLayoutManager(getContext(),2));
        casts =new ArrayList<>();
        castsAdapter =new ShowDetailAdapter(getActivity(), casts);
        seasonView.setAdapter(castsAdapter);
        fetchCasts();
    }
    public void fetchCasts()
    {
        DBManager.fetchCast(id, new OnDownloadComplete<ArrayList<Cast>>() {
            @Override
            public void onDownloadComplete(ArrayList<Cast> result) {
                if(result!=null)
                {
                    casts.addAll(result);
                    castsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     /*   if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
