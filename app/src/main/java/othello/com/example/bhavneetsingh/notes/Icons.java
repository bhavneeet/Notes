package othello.com.example.bhavneetsingh.notes;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bhavneet singh on 12-Feb-18.
 */

public class Icons {
    private Integer image;
    private String text;
    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Icons(String text,Integer image)
    {
        this.text=text;
        this.image=image;
    }
}