package othello.com.example.bhavneetsingh.Triads.Database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import othello.com.example.bhavneetsingh.Triads.Movies.*;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.News.*;
import othello.com.example.bhavneetsingh.Triads.Posts.*;
import othello.com.example.bhavneetsingh.Triads.Main.*;
import othello.com.example.bhavneetsingh.Triads.User.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    static Retrofit retrofit=new Retrofit.Builder().baseUrl("https://triads.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
    static UserApi userApi=retrofit.create(UserApi.class);
    //Registeration
    public static void registerUser(User user, final OnDownloadComplete<User> downloadComplete)
    {
     Call<User>call=userApi.registerUser(user.getName(),user.getUser_id(),user.getPassword(),user.getProfilePictureUrl());
     call.enqueue(new Callback<User>() {
         @Override
         public void onResponse(Call<User> call, Response<User> response) {
          downloadComplete.onDownloadComplete(response.body());
         }
         @Override
         public void onFailure(Call<User> call, Throwable t) {
             downloadComplete.onDownloadComplete(null);
         }
     });
    }
    //Searching User
    public static void searchUser(String name, final OnDownloadComplete<ArrayList<User>> downloadComplete)
    {
        Call<ArrayList<User>>call=userApi.searchUser(name);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                downloadComplete.onDownloadComplete(null);
            }
        });
    }
    //public void fetchNews
    public static void fetchNews(User user, final OnDownloadComplete<ArrayList<News>> downloadComplete)
    {
        Call<ArrayList<News>>call=userApi.fetchNews(user.getUser_id());
        call.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                downloadComplete.onDownloadComplete(null);
            }
        });
    }
    //Fetch Casts
    public static void fetchCast(String id, final OnDownloadComplete<ArrayList<Cast>>downloadComplete)
    {
        Call<ArrayList<Cast>> call=userApi.fetchCast(id);
        call.enqueue(new Callback<ArrayList<Cast>>() {
            @Override
            public void onResponse(Call<ArrayList<Cast>> call, Response<ArrayList<Cast>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Cast>> call, Throwable t) {
                Log.d("season_error",t.getLocalizedMessage());
                downloadComplete.onDownloadComplete(null);
            }
        });
    }
    //Fetch Seasons
    public static void fetchSeasons(String id, final OnDownloadComplete<ArrayList<Season>>downloadComplete)
    {
        Call<ArrayList<Season>> call=userApi.fetchSeason(id);
        call.enqueue(new Callback<ArrayList<Season>>() {
            @Override
            public void onResponse(Call<ArrayList<Season>> call, Response<ArrayList<Season>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Season>> call, Throwable t) {
                Log.d("season_error",t.getLocalizedMessage());
            downloadComplete.onDownloadComplete(null);
            }
        });
    }
    //fetching movies
    public static void fetchMovies(User user, final OnDownloadComplete<ArrayList<Movie>>downloadComplete)
    {
        Call<ArrayList<Movie>>call=userApi.fetchMovies(user.getUser_id(),1);
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
            downloadComplete.onDownloadComplete(null);
            }
        });
    }
    public static void fetchMovies(User user, int page, final OnDownloadComplete<ArrayList<Movie>>downloadComplete)
    {
        Call<ArrayList<Movie>>call=userApi.fetchMovies(user.getUser_id(),page);
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                downloadComplete.onDownloadComplete(null);
            }
        });
    }
    //Fetch Post List
    public static ArrayList<Posts> fetchList(User user, int limit, int offset, final OnDownloadComplete<ArrayList<Posts>> downloadComplete)
    {
         final ArrayList<Posts>posts=new ArrayList<>();
        Call<ArrayList<Posts>>call=userApi.fetchPosts(user.getUser_id(),limit,offset,user.getCategory());
        call.enqueue(new Callback<ArrayList<Posts>>() {
            @Override
            public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
                   downloadComplete.onDownloadComplete(null);
            }
        });
        return posts;
    }
    public static void fetchLocations(final OnDownloadComplete<ArrayList<User>>downloadComplete)
    {
        Call<ArrayList<User>>call=userApi.fetchUsers();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
               downloadComplete.onDownloadComplete(null);
            }
        });
    }
    public static ArrayList<Posts> fetchSimilarNotes(SQLiteOpenHelper db_helper, String username)
    {
        ArrayList<Posts>postsArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String query="SELECT * "
                +"FROM "+MyDatabase.KEY_TABLE+" as T ,"+ User.TABLE+" as S"
                +" WHERE (T."+ User.USER_ID+" = "+"S."+ User.USER_ID+" and S."+ User.NAME+" like ?)"
                +" ORDER BY "+MyDatabase.KEY_DATE+" DESC";

        Cursor cursor=db.rawQuery(query,new String[]{username.substring(0,3)+'%'});
        while(cursor.moveToNext())
        {
            String content=cursor.getString(cursor.getColumnIndex(MyDatabase.KEY_CONTEXT));
            int smiley=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_SMILEY));
            long id=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_ID));
            String usern=cursor.getString(cursor.getColumnIndex(User.NAME));
            String userp=cursor.getString(cursor.getColumnIndex(User.PASSWORD));
            String useri=cursor.getString(cursor.getColumnIndex(User.USER_ID));
            Posts posts=new Posts(new User(useri,usern,userp),content,0);
            posts.setId(id);
            postsArrayList.add(posts);
        }
        return postsArrayList;
    }
    // fetch only user notes
    public static ArrayList<Posts> fetchMyNotes(SQLiteOpenHelper db_helper, User user)
    {
        ArrayList<Posts>postsArrayList=new ArrayList<>();
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String query="SELECT * "
                +"FROM "+MyDatabase.KEY_TABLE+" as T ,"+ User.TABLE+" as S"
                +" WHERE (T."+ User.USER_ID+" = "+"S."+ User.USER_ID+" and T."+ User.USER_ID+" = ?)"
                +" ORDER BY "+MyDatabase.KEY_DATE+" DESC";

        Cursor cursor=db.rawQuery(query,new String[]{user.getUser_id()});
        while(cursor.moveToNext())
        {
            String content=cursor.getString(cursor.getColumnIndex(MyDatabase.KEY_CONTEXT));
            int smiley=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_SMILEY));
            long id=cursor.getInt(cursor.getColumnIndex(MyDatabase.KEY_ID));
            String usern=cursor.getString(cursor.getColumnIndex(User.NAME));
            String userp=cursor.getString(cursor.getColumnIndex(User.PASSWORD));
            String useri=cursor.getString(cursor.getColumnIndex(User.USER_ID));
            Posts posts=new Posts(new User(useri,usern,userp),content,0);
            posts.setId(id);
            postsArrayList.add(posts);
        }
        return postsArrayList;

    }
    //Add Post
    public static Posts addPost(Posts post,OnDownloadComplete<Posts> downloadComplete)
    {
        String user_id=post.getUser().getUser_id();
        String url=null;
        if(post.getImgUrl()!=null)
            url=post.getImgUrl().toString();
        Call<Posts>user=userApi.addPost(user_id,post.getContent(),url);
        user.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {

            }
        });
        return null;
    }
    //Fetching User
    public static void fetchUser(String user_id, String password, final OnDownloadComplete<User>downloadComplete, final OnDownloadComplete<Boolean>failed)
    {
        Call<User>user=userApi.getUser(user_id,password);
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                failed.onDownloadComplete(false);
            }
        });
    }
    //deleteFollower
    public static void deleteFollower(String source, String target, final OnDownloadComplete<Triads.Follower>onDownloadComplete)
    {
        Call<Triads.Follower> call=userApi.deleteFollower(source,target);
        call.enqueue(new Callback<Triads.Follower>() {
            @Override
            public void onResponse(Call<Triads.Follower> call, Response<Triads.Follower> response) {
                onDownloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<Triads.Follower> call, Throwable t) {
                onDownloadComplete.onDownloadComplete(null);
            }
        });
    }
    //get User
    public static void getUserPublic(String user_id, final OnDownloadComplete<User>onDownloadComplete)
    {
        Call<User>call=userApi.getUserPublic(user_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                onDownloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    //deleteFollower
    public static void addFollower(String source, String target, final OnDownloadComplete<Triads.Follower>onDownloadComplete)
    {
        Call<Triads.Follower> call=userApi.addFollower(source,target);
        call.enqueue(new Callback<Triads.Follower>() {
            @Override
            public void onResponse(Call<Triads.Follower> call, Response<Triads.Follower> response) {
                onDownloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<Triads.Follower> call, Throwable t) {
                 onDownloadComplete.onDownloadComplete(null);
            }
        });
    }
    //Fetching Followers
    public static ArrayList<User> fetchFollowers(User current_user, final OnDownloadComplete<ArrayList<User>> downloadComplete)
    {
        ArrayList<User>list=new ArrayList<>();
        Call<ArrayList<User>>call=userApi.fetchFollowers(current_user.getUser_id());
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
        return list;
    }

    //Update Post
    public static void editPost(Posts post, final OnDownloadComplete<Posts> downloadComplete)
    {
    String url="null";
    if(post.getImgUrl()!=null)
        url=post.getImgUrl().toString();
    Call<Posts> call=userApi.editPost(post.getContent(),post.getId(),post.getSmiley_id(),url);
    call.enqueue(new Callback<Posts>() {
        @Override
        public void onResponse(Call<Posts> call, Response<Posts> response) {
            downloadComplete.onDownloadComplete(response.body());
        }

        @Override
        public void onFailure(Call<Posts> call, Throwable t) {

        }
    });
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
    public static User containsUser(SQLiteOpenHelper db_helper, String user_id)
    {
        SQLiteDatabase db=db_helper.getReadableDatabase();
        String expensearg[]={user_id};
        Cursor cursor=db.query(User.TABLE,null, User.USER_ID+" = ?",expensearg,null,null,null);
        User user=null;
        if(cursor.moveToNext())
        user=new User(cursor.getString(cursor.getColumnIndex(User.USER_ID)),cursor.getString(cursor.getColumnIndex(User.NAME)),cursor.getString(cursor.getColumnIndex(User.PASSWORD)));
        return  user;
    }
    public static  boolean addUser(SQLiteOpenHelper db_helper, User user)
    {
        SQLiteDatabase db=db_helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        boolean check=user.getName().isEmpty()||user.getPassword().isEmpty()||user.getPassword().isEmpty();
        if(check)
            return false;
        contentValues.put(User.USER_ID,user.getUser_id());
        contentValues.put(User.NAME,user.getName());
        contentValues.put(User.PASSWORD,user.getPassword());
        long id=db.insert(User.TABLE,null,contentValues);
        return !(id==-1);
    }
    //getting followers list
    public static void fetchFollowers(String source, final OnDownloadComplete<ArrayList<User>>downloadComplete)
    {
        Call<ArrayList<User>>call=userApi.fetchFollowers(source);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                downloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
              downloadComplete.onDownloadComplete(null);
            }
        });
    }
}