package othello.com.example.bhavneetsingh.Triads.Posts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PostsDOE {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(ArrayList<Posts>postList);
    @Query("select * from Posts")
    List<Posts> fetchPosts();
    @Query("DELETE FROM Posts where user_id  = :user_id")
    void deletePosts(String user_id);
}