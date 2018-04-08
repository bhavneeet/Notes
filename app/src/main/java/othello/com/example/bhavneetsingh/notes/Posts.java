package othello.com.example.bhavneetsingh.notes;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

@Entity
public class Posts {
    public static final String KEY_CONTEXT="content",KEY_ID="post_id",KEY_SMILEY="smiley",KEY_TABLE="post_table",KEY_DATE="current",IMAGE="imageurl";
    private String context;
    private String smiley;
    @PrimaryKey
    private long post_id;
    private String image;
    private String category;
    public String getDate_edited() {
        return date_edited;
    }

    public void setDate_edited(String date_edited) {
        this.date_edited = date_edited;
    }
    private int likes;
    @Ignore
    private ArrayList<String>comment;
    @Ignore
    private MyDatabase.User user;
    private String user_id,name,profile_picture;
    private String date_edited;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSmiley() {
        return smiley;
    }

    public void setSmiley(String smiley) {
        this.smiley = smiley;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
   public Posts()
   {
      user=new MyDatabase.User(user_id,name,"");
      user.setProfilePictureUrl(this.profile_picture);
   }
    @Ignore
    private Bitmap bitmap;
    public String getImgUrl() {
        return image;
    }

    public void setImgUrl(String imgUrl)  {
            try{
                this.image = imgUrl;
            }
            catch (Exception e)
            {

            }
    }
    public Posts(MyDatabase.User user,String content,long id) {
        this.context = content;
        this.user=user;
        this.smiley ="none";
        this.post_id=id;
        image=null;
    }

    public String getContent() {
        return context;
    }

    public void setContent(String content) {
        this.context = content;
    }

    public String getSmiley_id() {
        return smiley;
    }

    public void setSmiley_id(String smiley_id) {
        this.smiley = smiley_id;
    }

    public long getId() {
        return post_id;
    }

    public void setId(long id) {
        this.post_id = id;
    }

    public MyDatabase.User getUser()
    {
        if(user.getUser_id()==null)
        {
            user.setName(name);
            user.setUser_id(user_id);
            user.setProfilePictureUrl(profile_picture);
            user.setCategory(category);
        }
        return user;
    }

    public void setUser(MyDatabase.User user) {
        this.user = user;
    }

}