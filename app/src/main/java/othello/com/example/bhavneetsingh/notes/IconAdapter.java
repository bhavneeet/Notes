package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class IconAdapter extends ArrayAdapter<String> {
    Activity context;
    ArrayList<String>text;
    ArrayList<Integer>images;
    public IconAdapter(Activity context,ArrayList<String>text,ArrayList<Integer>images)
    {
        super(context, R.layout.icon_list_layout,text);
        this.context=context;
        this.text=text;
        this.images=images;
    }
    public View getView(int pos, View convertView, ViewGroup group)
    {
        Holder holder;
        if(convertView==null)
        {
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(R.layout.icon_list_layout,group,false);
             holder=new Holder();
            holder.text=(TextView)convertView.findViewById(R.id.icon_name);
            holder.image=(ImageView)convertView.findViewById(R.id.icon_image);
            convertView.setTag(holder);
        }
        else
        {
            holder=(Holder)convertView.getTag();
        }


        holder.text.setText(text.get(pos));
        holder.image.setImageResource(images.get(pos));
        return convertView;
    }
}
 class Holder
{
   TextView text;
   ImageView image;
}
