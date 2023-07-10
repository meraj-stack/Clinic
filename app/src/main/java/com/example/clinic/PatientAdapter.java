package com.example.clinic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class
PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private List<Patient> patientList = new ArrayList<>();
    private DatabaseReference databaseRef;
    private Context context;
    public PatientAdapter(Context context, DatabaseReference databaseRef){
        this.databaseRef = databaseRef;
        this.context = context;
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()
                     ) {
                    Patient patient = snapshot.getValue(Patient.class);

                    if(patient!=null){
                        patient.setUid(snapshot.getKey());
                        patientList.add(patient);

                    }

                    
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
    @NonNull
    @Override
    public PatientAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_data, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.txt_first.setText(patient.getFirstname());
        holder.txt_last.setText(patient.getLastname());
        holder.txt_mobile.setText(patient.getMobile());
        Glide.with(holder.img_patient.getContext()).load(patient.getProfile()).into(holder.img_patient);

        holder.img_showprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", patient.getUid());
                Fragment fragment = new ViewPatientFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((MainActivity3) context ).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        holder.img_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("UID", patient.getUid());
                Fragment fragment1 = new EditPatientFragment();
                fragment1.setArguments(bundle1);
                FragmentTransaction ft1 = ((MainActivity3) context).getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.container, fragment1);
                ft1.addToBackStack(null);
                ft1.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{
        CircularImageView img_patient;
        ImageView img_showprofile, img_editprofile;
        TextView txt_first, txt_last, txt_mobile;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_first = itemView.findViewById(R.id.txt_first);
            txt_last = itemView.findViewById(R.id.txt_last);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            img_patient = itemView.findViewById(R.id.img_patient);
            img_showprofile = itemView.findViewById(R.id.img_showprofile);
            img_editprofile = itemView.findViewById(R.id.img_editprofile);
        }
    }


}
