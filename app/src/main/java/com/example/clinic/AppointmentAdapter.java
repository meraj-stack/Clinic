package com.example.clinic;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList){
        this.context=context;
        this.appointmentList=appointmentList;
    }
    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentlist, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.txt_patientName.setText(appointment.getPateintname());
        holder.txt_doctorName.setText(appointment.getDrname());
        holder.txt_Date.setText(appointment.getDate());
        holder.txt_Time.setText(appointment.getTime());
        holder.txt_status.setText(appointment.getStatus());
        String status = appointment.getStatus();
        int bgcolor;

        if (status.equals("Completed")) {
            bgcolor = ContextCompat.getColor(context, R.color.progresscolor);
        } else if (status.equals("Cancelled")) {
            bgcolor = ContextCompat.getColor(context, R.color.cancelled);
        } else {
            // Default background color if status doesn't match any condition
            bgcolor = ContextCompat.getColor(context, R.color.frontcolor2);
        }

        holder.cardView.setBackgroundColor(bgcolor);

        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_appointment, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.menu_item_update:
                                Bundle bundle = new Bundle();
                                bundle.putString("key", appointment.getId());
                                Fragment fragment = new AppointmentUpdateFragment();
                                fragment.setArguments(bundle);
                                FragmentTransaction ft = ((MainActivity3) context).getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                                return true;
                            case R.id.menu_item_view:
                                return true;
                            default:
                                return false;

                        }

                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_patientName, txt_doctorName, txt_Date, txt_Time, txt_status;
        private ImageView  img_menu;
        private CardView cardView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_patientName = itemView.findViewById(R.id.text_patientname);
            txt_doctorName = itemView.findViewById(R.id.text_doctorname);
            txt_Date = itemView.findViewById(R.id.text_date);
            txt_Time = itemView.findViewById(R.id.text_time);
            txt_status = itemView.findViewById(R.id.txt_status);
            img_menu = itemView.findViewById(R.id.img_menu);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
