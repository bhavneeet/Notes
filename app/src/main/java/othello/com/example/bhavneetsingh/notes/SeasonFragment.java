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

public class SeasonFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private String id;
    private RecyclerView seasonView;
    private ShowDetailAdapter seasonAdapter;
    private ArrayList<ShowDetail>seasons;
    private View view;
    @SuppressLint("ValidFragment")
    private SeasonFragment() {
        // Required empty public constructor
    }


    public static SeasonFragment newInstance(String id) {
        SeasonFragment fragment = new SeasonFragment();
        Bundle bundle=new Bundle();
        bundle.putString(MovieDetailActivity.ID,id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         id=getArguments().getString(MovieDetailActivity.ID);
        }

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
        seasons=new ArrayList<>();
        seasonAdapter=new ShowDetailAdapter(getContext(),seasons);
        seasonView.setAdapter(seasonAdapter);
       fetchSeasons();
    }
    public void fetchSeasons()
    {
     DBManager.fetchSeasons(id, new OnDownloadComplete<ArrayList<Season>>() {
         @Override
         public void onDownloadComplete(ArrayList<Season> result) {
             seasons.addAll(result);
             seasonAdapter.notifyDataSetChanged();
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
