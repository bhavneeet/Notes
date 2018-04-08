package othello.com.example.bhavneetsingh.notes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bhavneet singh on 02-Apr-18.
 */

public class ShowDetailAdapter extends RecyclerView.Adapter<ShowDetailAdapter.ShowDetailHolder>{

    public ArrayList<ShowDetail>showDetails;
    public Context context;

    public ShowDetailAdapter(Context context,ArrayList<ShowDetail> showDetails) {
        this.showDetails = showDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ShowDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView=inflater.inflate(R.layout.items_layout,parent,false);
        ShowDetailHolder showDetailHolder=new ShowDetailHolder(convertView);
        return showDetailHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowDetailHolder holder, int position) {
        int pos=holder.getAdapterPosition();
        ShowDetail showDetail=showDetails.get(pos);
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
