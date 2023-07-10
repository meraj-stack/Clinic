package com.example.clinic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllDoctorFragment extends Fragment {
    private RecyclerView doctor_recycler;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorlist;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_all_doctor, container, false);
        View view = inflater.inflate(R.layout.fragment_all_doctor, container, false);
        doctor_recycler = view.findViewById(R.id.doctor_recycler);
        doctorlist = new ArrayList<>();
        doctor_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctorAdapter = new DoctorAdapter(requireContext(), doctorlist);
        doctor_recycler.setAdapter(doctorAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ClinicUser");
        Query query = databaseReference.orderByChild("usertype").equalTo("Doctor");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()) {
                    String firstname = dataSnapshot.child("firstname").getValue(String.class);
                    String lastname = dataSnapshot.child("lastname").getValue(String.class);
                    String specialization = dataSnapshot.child("specialization").getValue(String.class);
                    String profile = dataSnapshot.child("imageurl").getValue(String.class);
                    Doctor doctor = new Doctor(firstname, lastname, specialization, profile);
                    if(doctor!=null){
                        doctor.setUid(dataSnapshot.getKey());
                        doctorlist.add(doctor);
                    }

                }
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }
}