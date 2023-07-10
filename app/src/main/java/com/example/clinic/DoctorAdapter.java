package com.example.clinic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<Doctor> doctorlist;
    private Context context;


    public DoctorAdapter(Context context, List<Doctor> doctorlist){
        this.doctorlist=doctorlist;
        this.context=context;

    }


    public interface OnDoctorClickListener {
        void onDoctorClick(int position);
    }
    @NonNull
    @Override
    public DoctorAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alldoctors, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.DoctorViewHolder holder, int position) {
        Doctor doctor = doctorlist.get(position);
        holder.txt_doctorname.setText("Dr. " + doctor.getFirstname() + " " + doctor.getLastname());
        holder.txt_special.setText(doctor.getSpecialization());
        Glide.with(holder.img_doctor.getContext()).load(doctor.getImage()).into(holder.img_doctor);
        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_docprofile, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_item_view:
                                Bundle bundle = new Bundle();
                                bundle.putString("uid", doctor.getUid());
                                Fragment fragment = new DoctorDetailsFragment();
                                fragment.setArguments(bundle);
                                FragmentTransaction ft = ((MainActivity2) context).getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, fragment);
                                ft.addToBackStack(null);
                                ft.commit();
                                return true;
                            case R.id.menu_item_edit:
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("Uid", doctor.getUid());
                                Fragment fragment1 = new EditDoctorFragment();
                                fragment1.setArguments(bundle1);
                                FragmentTransaction ft1 = ((MainActivity2) context).getSupportFragmentManager().beginTransaction();
                                ft1.replace(R.id.container, fragment1);
                                ft1.addToBackStack(null);
                                ft1.commit();
                                return true;
                            case R.id.menu_item_delete:
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
        return doctorlist.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder{
        ImageView img_doctor, img_menu;
        TextView txt_doctorname, txt_special;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            img_doctor = itemView.findViewById(R.id.img_doctor);
            txt_doctorname = itemView.findViewById(R.id.txt_doctorname);
            txt_special = itemView.findViewById(R.id.txt_special);
            img_menu = itemView.findViewById(R.id.img_menu);
        }
    }
}
