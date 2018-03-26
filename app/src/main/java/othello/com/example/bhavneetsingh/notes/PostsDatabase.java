package othello.com.example.bhavneetsingh.notes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
/*
@Database(entities = {Posts.class},version = 1)*/
public abstract class PostsDatabase /*extends RoomDatabase */{
    public abstract PostsDOE getPostsDOE();
}
