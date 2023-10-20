package com.itsolution.electronichealthrecord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class adapter_for_others_report extends RecyclerView.Adapter<adapter_for_others_report.ViewHolder> {

    Context context;
    List<model_for_other_reports> modellist;

    public adapter_for_others_report(Context context, List<com.itsolution.electronichealthrecord.model_for_other_reports> modellist) {
        this.context = context;
        this.modellist = modellist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.other_report_holder,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.itsolution.electronichealthrecord.model_for_other_reports model_for_other_reports=modellist.get(position);
        holder.name.setText(model_for_other_reports.getName());
        Picasso.get().load(model_for_other_reports.getProfile_img()).into(holder.user_img);

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView user_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
            user_img=itemView.findViewById(R.id.user_img);

        }
    }
}
