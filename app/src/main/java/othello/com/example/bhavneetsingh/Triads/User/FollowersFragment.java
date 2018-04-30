package othello.com.example.bhavneetsingh.Triads.User;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.Main.MainActivity;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.R;


public class FollowersFragment extends Fragment {

    private User current_user;
    private ArrayList<User>usersList;
    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.current_user = MainActivity.getCurrentUser();
        usersList=new ArrayList<>();
/*        adapter=new ProfileAdapter(context,usersList,this,current_user);*/
    }


    ProfileAdapter adapter;
    @SuppressLint("ValidFragment")
    public FollowersFragment(User current_user) {

        DBManager.fetchFollowers(current_user, new OnDownloadComplete<ArrayList<User>>() {
            @Override
            public void onDownloadComplete(ArrayList<User> result) {
               usersList.clear();
               for(User user:result)
                   user.setCurrentFollowing(true);
               usersList.addAll(result);
               adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_followers, container, false);
        ListView listView=(ListView) view.findViewById(R.id.followlist);
        listView.setAdapter(adapter);
        return view;
    }

    /*@Override
    public void onClickFollowButton(View v, MyDatabase.User current_user, MyDatabase.User follower) {

    }*/
}