package com.example.clinic;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPatientFragment extends Fragment {
    private TextView txt_Fname, txt_Lname, txt_Mobile, txt_Sex, txt_Marital, txt_Bg, txt_Height, txt_Weight, txt_Address, txt_Phistory, txt_Email, txt_Dob;
    private ImageView img_pProfile;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_patient, container, false);
        txt_Fname = view.findViewById(R.id.txt_Fname);
        txt_Lname = view.findViewById(R.id.txt_Lname);
        txt_Bg = view.findViewById(R.id.txt_Bg);
        txt_Marital = view.findViewById(R.id.txt_Marital);
        txt_Weight = view.findViewById(R.id.txt_Weight);
        txt_Height= view.findViewById(R.id.txt_Height);
        txt_Email = view.findViewById(R.id.txt_Email);
        txt_Sex = view.findViewById(R.id.txt_Sex);
        txt_Mobile = view.findViewById(R.id.txt_Mobile);
        txt_Dob = view.findViewById(R.id.txt_Dob);
        txt_Phistory = view.findViewById(R.id.txt_Phistory);
        txt_Address = view.findViewById(R.id.txt_Address);
        img_pProfile = view.findViewById(R.id.img_pProfile);

        String uid = getArguments().getString("uid");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.getValue(Patient.class);
                txt_Fname.setText(patient.getFirstname());
                txt_Lname.setText(patient.getLastname());
                txt_Mobile.setText(patient.getMobile());
                txt_Email.setText(patient.getEmail());
                txt_Marital.setText(patient.getMaritalStatus());
                txt_Sex.setText(patient.getSex());
                txt_Address.setText(patient.getAddress());
                txt_Phistory.setText(patient.getPatientHistory());
                txt_Bg.setText(patient.getBloodGroup());
                txt_Dob.setText(patient.getDob());
                txt_Height.setText(String.valueOf(patient.getHeight()));
                txt_Weight.setText(String.valueOf(patient.getWeight()));
                Glide.with(requireContext()).load(patient.getProfile()).into(img_pProfile);
            }

            @Override

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}