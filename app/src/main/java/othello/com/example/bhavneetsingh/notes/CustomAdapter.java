package othello.com.example.bhavneetsingh.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter  {
    Context context;
    ArrayList<Icons>icons;
    public CustomAdapter(Context context,ArrayList<Icons> icons)
    {
        this.context=context;
        this.icons=icons;
    }
    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Icons getItem(int position) {
        return icons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IconHolder holder=new IconHolder();
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.icon_list_layout,parent,false);
            holder.imageView=(ImageView) convertView.findViewById(R.id.icon_image);
            holder.textView=(TextView)convertView.findViewById(R.id.icon_name);
            convertView.setTag(holder);
        }
        else
        {
            holder=(IconHolder)convertView.getTag();
        }
        holder.textView.setText(icons.get(position).getText());
        return convertView;    }
    public View  getDropDownView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IconHolder holder=new IconHolder();
        if(convertView==null)
        {
         convertView=inflater.inflate(R.layout.icon_list_layout,parent,false);
         holder.imageView=(ImageView) convertView.findViewById(R.id.icon_image);
         holder.textView=(TextView)convertView.findViewById(R.id.icon_name);
         convertView.setTag(holder);
        }
        else
        {
            holder=(IconHolder)convertView.getTag();
        }
        holder.textView.setText(icons.get(position).getText());
        return convertView;
    }
}
class IconHolder{
    ImageView imageView;
    TextView textView;
}