package othello.com.example.bhavneetsingh.notes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bhavneet singh on 17-Feb-18.
 */

public class Posts {
    private String content;
    private int smiley_id;
    long id;
    private ArrayList<String>comment;
    private MyDatabase.User user;
    public Posts(MyDatabase.User user,String content) {
        this.content = content;
        this.user=user;
        this.smiley_id = R.drawable.smiley;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSmiley_id() {
        return smiley_id;
    }

    public void setSmiley_id(int smiley_id) {
        this.smiley_id = smiley_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MyDatabase.User getUser() {
        return user;
    }

    public void setUser(MyDatabase.User user) {
        this.user = user;
    }

}