package othello.com.example.bhavneetsingh.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DBManager {
    //check connection
    public static boolean checkConnection(SQLiteOpenHelper db_helper,String source,String target)
    {
        SQLiteDatabase db=db_helper.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        String args[]={source,target};
        Cursor cursor=db.query(MyDatabase.Graph.TABLE,null,MyDatabase.Graph.SOURCE+" = ? and "+MyDatabase.Graph.TARGET+" = ?",args,null,null,null,null);
        return cursor.moveToNext();
    }
    //add Graph
    public static boolean addGraph(SQLiteOpenHelper db_helper,String source,String target)
    {
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatabase.Graph.SOURCE,source);
        contentValues.put(MyDatabase.Graph.TARGET,target);
        long id=db.insert(MyDatabase.Graph.TABLE,null,contentValues);
        return !(id==-1);
    }
    //delete row in Graph
    public static boolean deleteGraphEdge(SQLiteOpenHelper db_helper,String source,String target)
    {
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatabase.Graph.SOURCE,source);
        contentValues.put(MyDatabase.Graph.TARGET,target);
        String arr[]={source,target};
        long id=db.delete(MyDatabase.Graph.TABLE, MyDatabase.Graph.SOURCE+" = ? and "+MyDatabase.Graph.TARGET+" = ?",arr);
        return !(id==-1);
    }
    //User List
    public static ArrayList<MyDatabase.User> fetchUserList(SQLiteOpenHelper db_helper, MyDatabase.User user)
    {
        ArrayList<MyDatabase.User>usersArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String query="SELECT * "
                +"FROM "+MyDatabase.User.TABLE
                +" WHERE "+MyDatabase.User.USER_ID+" != ?";

        Cursor cursor=db.rawQuery(query,new String[]{user.getUser_id()});
        while(cursor.moveToNext())
        {
            String usern=cursor.getString(cursor.getColumnIndex(MyDatabase.User.NAME));
            String userp=cursor.getString(cursor.getColumnIndex(MyDatabase.User.PASSWORD));
            String useri=cursor.getString(cursor.getColumnIndex(MyDatabase.User.USER_ID));
            usersArrayList.add(new MyDatabase.User(useri,usern,userp));
        }
        return usersArrayList;
    }

    //Fetch Post List
    public static ArrayList<Posts> fetchList(MyDatabase.User user,OnDownloadComplete downloadComplete)
    {
         final ArrayList<Posts>posts=new ArrayList<>();
        Networking<String,ArrayList<Posts>> networking=new Networking<>(new InternetActivity<String,ArrayList<Posts>>() {

            @Override
            public ArrayList<Posts> doInBackground(String... strings) {
                String url=strings[0];
                String result=Networking.connectionResult(url);
             return getPosts(result,true);
            }
            @Override

            public void onPostExecute(ArrayList<Posts> result) {
            }
            @Override
            public void onPreExecute(ArrayList<Posts> result) {

            }
        },downloadComplete);
        String url="https://triads.herokuapp.com/userPosts?id="+user.getUser_id();
        networking.execute(url);
        return posts;
    }
    public static ArrayList<Posts> fetchSimilarNotes(SQLiteOpenHelper db_helper, String username)
    {
        ArrayList<Posts>postsArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String query="SELECT * "
                +"FROM "+MyDatabase.KEY_TABLE+" as T ,"+MyDatabase.User.TABLE+" as S"
                +" WHERE (T."+ MyDatabase.User.USER_ID+" = "+"S."+MyDatabase.User.USER_ID+" and S."+MyDatabase.User.NAME+" like ?)"
                +" ORDER BY "+MyDatabase.KEY_DATE+" DESC";

        Cursor cursor=db.rawQuery(query,new String[]{username.substring(0,3)+'%'});
        while(cursor.moveToNext())
        {
            String content=cursor.getString(cursor.getColumnIndex(MyDatabase.KEY_CONTEXT));
            int smiley=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_SMILEY));
            long id=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_ID));
            String usern=cursor.getString(cursor.getColumnIndex(MyDatabase.User.NAME));
            String userp=cursor.getString(cursor.getColumnIndex(MyDatabase.User.PASSWORD));
            String useri=cursor.getString(cursor.getColumnIndex(MyDatabase.User.USER_ID));
            Posts posts=new Posts(new MyDatabase.User(useri,usern,userp),content,0);
            posts.setSmiley_id(smiley);
            posts.setId(id);
            postsArrayList.add(posts);
        }
        return postsArrayList;
    }
    // fetch only user notes
    public static ArrayList<Posts> fetchMyNotes(SQLiteOpenHelper db_helper, MyDatabase.User user)
    {
        ArrayList<Posts>postsArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String query="SELECT * "
                +"FROM "+MyDatabase.KEY_TABLE+" as T ,"+MyDatabase.User.TABLE+" as S"
                +" WHERE (T."+ MyDatabase.User.USER_ID+" = "+"S."+MyDatabase.User.USER_ID+" and T."+MyDatabase.User.USER_ID+" = ?)"
                +" ORDER BY "+MyDatabase.KEY_DATE+" DESC";

        Cursor cursor=db.rawQuery(query,new String[]{user.getUser_id()});
        while(cursor.moveToNext())
        {
            String content=cursor.getString(cursor.getColumnIndex(MyDatabase.KEY_CONTEXT));
            int smiley=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_SMILEY));
            long id=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_ID));
            String usern=cursor.getString(cursor.getColumnIndex(MyDatabase.User.NAME));
            String userp=cursor.getString(cursor.getColumnIndex(MyDatabase.User.PASSWORD));
            String useri=cursor.getString(cursor.getColumnIndex(MyDatabase.User.USER_ID));
            Posts posts=new Posts(new MyDatabase.User(useri,usern,userp),content,0);
            posts.setSmiley_id(smiley);
            posts.setId(id);
            postsArrayList.add(posts);
        }
        return postsArrayList;

    }
    //Add Post
    public static Posts addPost(MyDatabase.User user,String context,OnDownloadComplete<Posts> downloadComplete)
    {
        Networking<String,Posts> networking=new Networking<>(new InternetActivity<String,Posts>() {

            @Override
            public Posts doInBackground(String... strings) {
                Posts post=null;
                String url=strings[0];
                String result=Networking.connectionResult(url);
                ArrayList<Posts>posts=getPosts(result,false);
                try {
                    post=posts.get(0);
                }
                catch (Exception e)
                {

                }
                finally {
                    return post;
                }
            }
            @Override

            public void onPostExecute(Posts result) {
            }
            @Override
            public void onPreExecute(Posts result) {

            }
        },downloadComplete);
        String url="https://triads.herokuapp.com/insert?id="+user.getUser_id()+"&post="+context;
        networking.execute(url);
        return null;
    }
    //Delete Post
    public static void delete(long post_id,OnDownloadComplete<Posts>downloadComplete)
    {
        Networking<String,Posts> networking=new Networking<>(new InternetActivity<String, Posts>() {
            @Override
            public Posts doInBackground(String... strings) {
                String url=strings[0];
                String result=Networking.connectionResult(url);
                return  null;
            }
            @Override
            public void onPostExecute(Posts result) {

            }
            @Override
            public void onPreExecute(Posts result) {

            }
        }, downloadComplete);
        String url="https://triads.herokuapp.com/delete?post_id="+post_id;
        networking.execute(url);
    }
    //Update Post
    public static void editPost(Posts post,OnDownloadComplete<Posts> downloadComplete)
    {
        Networking<String,Posts> networking=new Networking<>(new InternetActivity<String, Posts>() {
            @Override
            public Posts doInBackground(String... strings) {
                String url=strings[0];
                String result=Networking.connectionResult(url);
                return  null;
            }

            @Override
            public void onPostExecute(Posts result) {

            }

            @Override
            public void onPreExecute(Posts result) {

            }
        }, downloadComplete);
        String url="https://triads.herokuapp.com/edit?post_id="+post.getId()+"&context="+post.getContent()+"&smiley="+post.getSmiley_id()+"&image="+post.getImgUrl();
        networking.execute(url);
     }
    public static MyDatabase.Comment addComment(SQLiteOpenHelper db_helper,Bundle bundle)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatabase.Comment.COMMENT_COLUMN,bundle.getString(MyDatabase.Comment.COMMENT_COLUMN));
        contentValues.put(MyDatabase.Comment.DATE_TIME,dateFormat.format(date));
        contentValues.put(MyDatabase.Comment.POST_ID,bundle.getLong(MyDatabase.Comment.POST_ID));
        long id=db.insert(MyDatabase.Comment.TABLE_NAME,null,contentValues);
        MyDatabase.Comment comment=new MyDatabase.Comment(bundle.getLong(MyDatabase.Comment.POST_ID));
        comment.setComment(bundle.getString(MyDatabase.Comment.COMMENT_COLUMN));
        comment.setComment_id(id);
        return comment;
    }
    public static ArrayList<MyDatabase.Comment> fetchCommentList(SQLiteOpenHelper db_helper,long post_id)
    {
        ArrayList<MyDatabase.Comment>commentsArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String expensearg[]={post_id+""};
        Cursor cursor=db.query(MyDatabase.Comment.TABLE_NAME,null,MyDatabase.Comment.POST_ID+" = ?",expensearg,null,null,MyDatabase.Comment.DATE_TIME+" desc");
        while(cursor.moveToNext())
        {
            String content=cursor.getString(cursor.getColumnIndex(MyDatabase.Comment.COMMENT_COLUMN));
            MyDatabase.Comment comment=new MyDatabase.Comment(post_id);
            comment.setComment(content);
            commentsArrayList.add(comment);
        }
        return commentsArrayList;
    }
    public static MyDatabase.User containsUser(SQLiteOpenHelper db_helper, String user_id)
    {
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String expensearg[]={user_id};
        Cursor cursor=db.query(MyDatabase.User.TABLE,null,MyDatabase.User.USER_ID+" = ?",expensearg,null,null,null);
        MyDatabase.User user=null;
        if(cursor.moveToNext())
        user=new MyDatabase.User(cursor.getString(cursor.getColumnIndex(MyDatabase.User.USER_ID)),cursor.getString(cursor.getColumnIndex(MyDatabase.User.NAME)),cursor.getString(cursor.getColumnIndex(MyDatabase.User.PASSWORD)));
        return  user;
    }
    public static  boolean addUser(SQLiteOpenHelper db_helper, MyDatabase.User user)
    {
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        boolean check=user.getName().isEmpty()||user.getPassword().isEmpty()||user.getPassword().isEmpty();
        if(check)
            return false;
        contentValues.put(MyDatabase.User.USER_ID,user.getUser_id());
        contentValues.put(MyDatabase.User.NAME,user.getName());
        contentValues.put(MyDatabase.User.PASSWORD,user.getPassword());
        long id=db.insert(MyDatabase.User.TABLE,null,contentValues);
        return !(id==-1);
    }
    //getting followers list
    public static void getFollowers(String user_id,OnDownloadComplete<HashMap<String,Integer>>downloadComplete)
    {
        Networking<String,HashMap<String,Integer>> networking=new Networking<>(new InternetActivity<String,HashMap<String, Integer>>() {

            @Override
            public HashMap<String, Integer> doInBackground(String... args) {
                String url=args[0];
                String result=Networking.connectionResult(url);
                return  getFollowers(result);
            }

            @Override
            public void onPostExecute(HashMap<String, Integer> result) {

            }

            @Override
            public void onPreExecute(HashMap<String, Integer> result) {

            }
        }, downloadComplete);
        String url="https://triads.herokuapp.com/followers?source="+user_id;
        networking.execute(url);
    }
    public static void addFollower(String source,String target)
    {
        Networking<String,Boolean> networking=new Networking<>(new InternetActivity<String, Boolean>() {
            @Override
            public Boolean doInBackground(String... args) {
                String url=args[0];
                String result=Networking.connectionResult(url);
                return null;
            }

            @Override
            public void onPostExecute(Boolean result) {

            }

            @Override
            public void onPreExecute(Boolean result) {

            }
        }, new OnDownloadComplete<Boolean>() {
            @Override
            public void onDownloadComplete(Boolean result) {

            }
        });
        String url="https://triads.herokuapp.com/addFollower?source="+source+"&target="+target;
        networking.execute(url);
    }
    public static void deleteFollower(String source,String target)
    {

        Networking<String,Boolean> networking=new Networking<>(new InternetActivity<String, Boolean>() {
            @Override
            public Boolean doInBackground(String... args) {
                String url=args[0];
                String result=Networking.connectionResult(url);
                return null;
            }

            @Override
            public void onPostExecute(Boolean result) {

            }

            @Override
            public void onPreExecute(Boolean result) {

            }
        }, new OnDownloadComplete<Boolean>() {
            @Override
            public void onDownloadComplete(Boolean result) {

            }
        });
        String url="https://triads.herokuapp.com/deleteFollower?source="+source+"&target="+target;
        networking.execute(url);
    }
    public static HashMap<String,Integer> getFollowers(String result)
    {
        HashMap<String,Integer>followers=new HashMap<>();
        try{
        JSONArray array=new JSONArray(result);
        for(int i=0;i<array.length();i++)
        {
            JSONObject item=array.getJSONObject(i);
            followers.put(item.getString("target"),item.getInt("weight"));
        }
        }
        catch (Exception e)
        {

        }
        return followers;
    }
    //Getting Posts
    public static ArrayList<Posts> getPosts(String result,boolean select)
    {
        ArrayList<Posts> postsList =new ArrayList<>();
        try{
            JSONArray posts=new JSONArray(result);
            for(int i=0;i<posts.length();i++)
            {
                JSONObject item=posts.getJSONObject(i);
                String user_name=item.getString("name");
                String password="";
                String user_id=item.getString("user_id");
                String context=item.getString("context");
                long post_id=item.getLong("post_id");
                int smiley=item.getInt("smiley");
                String img=item.getString("image");
                String url=item.getString("profile_picture");
                MyDatabase.User user=new MyDatabase.User(user_id,user_name,password);
                user.setProfilePictureUrl(url);
                Posts post=new Posts(user,context,post_id);
                post.setId(post_id);
                    post.setImgUrl(img);
                post.setSmiley_id(smiley);
                postsList.add(post);
            }
        }
        catch(Exception e)
        {
            Log.d("error_json",e.getLocalizedMessage());
        }
        return  postsList;
    }
}