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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Networking<ArrayList<Posts>> networking=new Networking<>(new InternetActivity<ArrayList<Posts>>() {

            @Override
            public ArrayList<Posts> doInBackground(String... strings) {
                String url=strings[0];
                String result=Networking.connectionResult(url);
             return getPosts(result);
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
            Posts posts=new Posts(new MyDatabase.User(useri,usern,userp),content);
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
            Posts posts=new Posts(new MyDatabase.User(useri,usern,userp),content);
            posts.setSmiley_id(smiley);
            posts.setId(id);
            postsArrayList.add(posts);
        }
        return postsArrayList;

    }
    //Add Post
    public static Posts addPost(SQLiteOpenHelper db_helper, Bundle bundle,MyDatabase.User current_user)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatabase.KEY_CONTEXT,bundle.getString(MyDatabase.KEY_CONTEXT));
        contentValues.put(MyDatabase.KEY_SMILEY,bundle.getInt(MyDatabase.KEY_SMILEY));
        contentValues.put(MyDatabase.KEY_DATE,dateFormat.format(date));
        contentValues.put(MyDatabase.User.USER_ID,current_user.getUser_id());
        long id = db.insert(MyDatabase.KEY_TABLE,null,contentValues);
        Log.d("Name",current_user.getName());
        Posts posts=new Posts(current_user,bundle.getString(MyDatabase.KEY_CONTEXT));
        posts.setSmiley_id(bundle.getInt(MyDatabase.KEY_SMILEY));
        posts.setId(id);
        return posts;
    }
    //Delete Post
    public static void delete(SQLiteOpenHelper db_helper,long id)
    {
        SQLiteDatabase db=db_helper.getWritableDatabase();
        String arr[]=new String[1];
        arr[0]=id+"";
        db.delete(MyDatabase.KEY_TABLE,MyDatabase.KEY_ID+" = ?",arr);

    }
    //Update Post
    public static void update(SQLiteOpenHelper db_helper,Posts post)
    {

        SQLiteDatabase db=db_helper.getWritableDatabase();
        long id=post.getId();
        String text=post.getContent();
        int smileyId=post.getSmiley_id();
        Bundle bundle=new Bundle();
        bundle.putString(MyDatabase.KEY_CONTEXT, text);
        bundle.putInt(MyDatabase.KEY_SMILEY, smileyId);
        String arr[]=new String[1];
        arr[0]=id+"";
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatabase.KEY_CONTEXT,bundle.getString(MyDatabase.KEY_CONTEXT));
        contentValues.put(MyDatabase.KEY_SMILEY,bundle.getInt(MyDatabase.KEY_SMILEY));
        contentValues.put(MyDatabase.User.USER_ID,post.getUser().getUser_id());
        db.update(MyDatabase.KEY_TABLE,contentValues,MyDatabase.KEY_ID+" = ?",arr);

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
    //Getting Posts
    public static ArrayList<Posts> getPosts(String result)
    {
        ArrayList<Posts> postsList =new ArrayList<>();
        try{
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
        }
        catch(Exception e)
        {
            Log.d("error",e.getLocalizedMessage());
        }
        return  postsList;
    }
}