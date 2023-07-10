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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DoctorDetailsFragment extends Fragment {
    TextView txt_fname, txt_lname, txt_Special, txt_Dob, txt_Address, txt_city, txt_Country,txt_doctorName, txt_Gender, txt_Bio, txt_Mobile, txt_Language;
    ImageView img_docprofile;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_details, container, false);
        txt_fname = view.findViewById(R.id.txt_Fname);
        txt_lname = view.findViewById(R.id.txt_Lname);
        txt_Special = view.findViewById(R.id.txt_Special);
        txt_Dob = view.findViewById(R.id.txt_Dob);
        txt_city = view.findViewById(R.id.txt_City);
        txt_Country = view.findViewById(R.id.txt_Country);
        txt_Address = view.findViewById(R.id.txt_Address);
        txt_Language = view.findViewById(R.id.txt_Language);
        txt_doctorName = view.findViewById(R.id.txt_DoctorName);
       txt_Mobile = view.findViewById(R.id.txt_Mobile);
        txt_Gender= view.findViewById(R.id.txt_Gender);
        txt_Bio = view.findViewById(R.id.txt_Bio);
        img_docprofile = view.findViewById(R.id.img_Docprofile);
        String uid = getArguments().getString("uid");

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("ClinicUser").child(uid);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClinicUser clinicUser = snapshot.getValue(ClinicUser.class);
                txt_fname.setText(clinicUser.getFirstname());
                txt_lname.setText(clinicUser.getLastname());
                txt_Special.setText(clinicUser.getSpecialization());
                txt_Mobile.setText(clinicUser.getContact());
                txt_Dob.setText(clinicUser.getDob());
                txt_Language.setText(clinicUser.getLanguage());
                txt_Address.setText(clinicUser.getAddress());
                txt_city.setText(clinicUser.getCity());
                txt_Country.setText(clinicUser.getCountry());
                txt_Gender.setText(clinicUser.getGender());
                txt_Bio.setText(clinicUser.getBio());
                txt_doctorName.setText("Dr. " + clinicUser.getFirstname() + " " + clinicUser.getLastname());
                Glide.with(requireContext()).load(clinicUser.getImageurl()).into(img_docprofile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}