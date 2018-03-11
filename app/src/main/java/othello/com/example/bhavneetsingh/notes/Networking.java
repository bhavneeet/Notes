package othello.com.example.bhavneetsingh.notes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Networking extends AsyncTask<String,Void,ArrayList<Posts>>{

    private OnDownloadComplete mlistener;

    public Networking(OnDownloadComplete mlistener) {
        this.mlistener = mlistener;
    }

    @Override
    protected ArrayList<Posts> doInBackground(String... strings) {
        ArrayList<Posts>postsList=new ArrayList<>();
        String urlLink=strings[0];
        try{
            URL url=new URL(urlLink);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream=httpURLConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            StringBuilder stringBuilder=new StringBuilder();
            while(scanner.hasNext())
            {
                stringBuilder.append(scanner.next());
            }
            String result=stringBuilder.toString();
            JSONArray posts=new JSONArray(result);
            for(int i=0;i<posts.length();i++)
            {
                JSONObject item=posts.getJSONObject(i);
                String user_name=item.getString("name");
                String password=item.getString("password");
                String user_id=item.getString("user_id");
                String context=item.getString("context");
                long post_id=item.getLong("post_id");
                Posts post=new Posts(new MyDatabase.User(user_id,user_name,password),context);
                post.setId(post_id);
                postsList.add(post);
            }
            inputStream.close();
            scanner.close();
        }
        catch(Exception e)
        {
            Log.d("error",e.getLocalizedMessage());
        }
        return postsList;
    }
    protected void onPostExecute(ArrayList<Posts>posts)
    {
        super.onPostExecute(posts);
        mlistener.onDownloadComplete(posts);
    }
}
interface OnDownloadComplete{
    void onDownloadComplete(ArrayList<Posts>posts);
}