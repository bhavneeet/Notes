package othello.com.example.bhavneetsingh.notes;

import android.graphics.Bitmap;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;


public class Posts {
    private String content;
    private int smiley_id;
    private long id;
    private URL imgUrl;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;
    public URL getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)  {
            try{
                this.imgUrl = new URL(imgUrl);
            }
            catch (Exception e)
            {

            }
    }

    private ArrayList<String>comment;
    private MyDatabase.User user;
    public Posts(MyDatabase.User user,String content,long id) {
        this.content = content;
        this.user=user;
        this.smiley_id = R.drawable.smiley;
        this.id=id;
        imgUrl=null;
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