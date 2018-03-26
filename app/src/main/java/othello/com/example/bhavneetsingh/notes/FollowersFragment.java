package othello.com.example.bhavneetsingh.notes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class FollowersFragment extends Fragment implements ProfileAdapter.FollowListener{

    private MyDatabase.User current_user;
    private ArrayList<MyDatabase.User>usersList;
    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.current_user = MainActivity.getCurrentUser();
        usersList=new ArrayList<>();
        adapter=new ProfileAdapter(context,usersList,this,current_user);
    }


    ProfileAdapter adapter;
    @SuppressLint("ValidFragment")
    public FollowersFragment(MyDatabase.User current_user) {

        DBManager.fetchFollowers(current_user, new OnDownloadComplete<ArrayList<MyDatabase.User>>() {
            @Override
            public void onDownloadComplete(ArrayList<MyDatabase.User> result) {
               usersList.clear();
               for(MyDatabase.User user:result)
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
    @Override
    public void onClickFollowButton(View v, MyDatabase.User current_user, MyDatabase.User follower) {
       if(v.getAlpha()==1)
       {
           final MyDatabase.User follow=follower;
           final View view=v;
          DBManager.deleteFollower(current_user, follower, new OnDownloadComplete<Boolean>() {
              @Override
              public void onDownloadComplete(Boolean result) {
                  if(result)
                  {
                      follow.setCurrentFollowing(false);
                      view.setAlpha((float)0.5);
                  }
              }
          });
       }
       else
       {
           final MyDatabase.User follow=follower;
           final View view=v;
           DBManager.addFollower(current_user, follower, new OnDownloadComplete<Boolean>() {
               @Override
               public void onDownloadComplete(Boolean result) {
                   if(result)
                   {
                       follow.setCurrentFollowing(true);
                       view.setAlpha((float)1);
                   }
               }
           });
       }
    }
}