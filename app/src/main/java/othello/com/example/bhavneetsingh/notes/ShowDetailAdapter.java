package othello.com.example.bhavneetsingh.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShowDetailAdapter extends RecyclerView.Adapter<ShowDetailAdapter.ShowDetailHolder>{

    public ArrayList<ShowDetail>showDetails;
    public Activity context;

    public ShowDetailAdapter(Activity context, ArrayList<ShowDetail> showDetails) {
        this.showDetails = showDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ShowDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView=inflater.inflate(R.layout.show_detail_layout,parent,false);
        ShowDetailHolder showDetailHolder=new ShowDetailHolder(convertView);
        return showDetailHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowDetailHolder holder, int position) {
        final int pos=holder.getAdapterPosition();
        ShowDetail showDetail=showDetails.get(pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WebActivity.class);
                String url="https://www.google.co.in/search?q=";
                String query=showDetails.get(pos).getName();
                String arr[]=query.split("");
                query="";
                for(String s:arr)
                {
                    query=query+"+"+s;
                }
                url=url+query;
                intent.putExtra(News.URL,url);
                context.startActivity(intent);
            }
        });
        holder.show_name.setText(showDetail.getName());
        Picasso.get().load(showDetail.getPoster()).fit().into(holder.show_image);
    }

    @Override
    public int getItemCount() {
        return showDetails.size();
    }

    class ShowDetailHolder extends RecyclerView.ViewHolder{
        TextView show_name;
        ImageView show_image;
        public ShowDetailHolder(View itemView)
        {
            super(itemView);
            show_name=(TextView)itemView.findViewById(R.id.show_name);
            show_image=(ImageView)itemView.findViewById(R.id.show_image);
        }
    }
}