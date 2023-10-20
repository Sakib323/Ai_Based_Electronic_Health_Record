package com.itsolution.electronichealthrecord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class adapter_to_get_data_OwnerDashboard extends RecyclerView.Adapter<adapter_to_get_data_OwnerDashboard.ViewHolder> {

    Context context;
    List<com.itsolution.electronichealthrecord.model_for_owner_dash> modellist;

    public adapter_to_get_data_OwnerDashboard(Context context, List<com.itsolution.electronichealthrecord.model_for_owner_dash> modellist) {
        this.context = context;
        this.modellist = modellist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_record,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.itsolution.electronichealthrecord.model_for_owner_dash model_for_owner_dash_variable=modellist.get(position);
        holder.specialization.setText(model_for_owner_dash_variable.getSpecialization());
        holder.physician_name.setText(model_for_owner_dash_variable.getPhysician_Name());
        holder.record_type.setText(model_for_owner_dash_variable.getRecord_Type());

        holder.note.setText(model_for_owner_dash_variable.getNote());
        holder.description.setText(model_for_owner_dash_variable.getDescription());
        holder.visit_date.setText(model_for_owner_dash_variable.getVisit_date());
        holder.diseases.setText(model_for_owner_dash_variable.getDiseases());

        holder.contact_info.setText(model_for_owner_dash_variable.getContact_info());
        Picasso.get().load(model_for_owner_dash_variable.getDr_img()).into(holder.dr_img);
        Picasso.get().load(model_for_owner_dash_variable.getReport_img()).into(holder.report_img);


    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView specialization,physician_name,record_type,note,description,visit_date,diseases,contact_info;
        ImageView dr_img,report_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            specialization=itemView.findViewById(R.id.specialization);
            physician_name=itemView.findViewById(R.id.physician_name);
            record_type=itemView.findViewById(R.id.record_type);
            note=itemView.findViewById(R.id.note);
            description=itemView.findViewById(R.id.description);
            visit_date=itemView.findViewById(R.id.visit_date);
            diseases=itemView.findViewById(R.id.diseases);
            dr_img=itemView.findViewById(R.id.dr_img);
            report_img=itemView.findViewById(R.id.report_img);
            contact_info=itemView.findViewById(R.id.contact_info);
        }
    }
}
