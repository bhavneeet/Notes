package othello.com.example.bhavneetsingh.notes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/users")
    Call<ArrayList<MyDatabase.User>> usersList();
    @GET("/userPosts")
    Call<ArrayList<Posts>> fetchPosts(@Query("id") String user_id,@Query("limit")int limit,@Query("offset")int offset,@Query("category")String category);
    @GET("/user")
    Call<MyDatabase.User> getUser(@Query("id") String user_id,@Query("password") String password);
    @GET("/userPublic")
    Call<MyDatabase.User> getUserPublic(@Query("id") String user_id);
    @GET("/insert")
    Call<Posts> addPost(@Query("id") String user_id,@Query("post") String post,@Query("image") String image);
    @GET("/edit")
    Call<Posts> editPost(@Query("context") String context,@Query("post_id") long post_id,@Query("smiley") String smiley,@Query("image") String image);
    @GET("/followers")
    Call<ArrayList<MyDatabase.User>> fetchFollowers(@Query("source")String user_id);
    @GET("deletefollower")
    Call<Void> deleteFollower(@Query("source")String source,@Query("target")String target);
    @GET("deletefollower")
    Call<Void> addFollower(@Query("source")String source,@Query("target")String target);
    @GET("userPosts/news")
    Call<ArrayList<News>> fetchNews(@Query("user_id")String user_id);
    @GET("userPosts/tvshows")
    Call<ArrayList<Movie>> fetchMovies(@Query("user_id")String user_id);
    @GET("/imdb/tvshows/seasons")
    Call<ArrayList<Season>> fetchSeason(@Query("id") String id);
    @GET("/imdb/tvshows/cast")
    Call<ArrayList<Cast>> fetchCast(@Query("user_id")String user_id);
    @GET("/locations")
    Call<ArrayList<UserLocation>> fetchLocation();
}