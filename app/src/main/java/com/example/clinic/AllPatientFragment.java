package com.example.clinic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllPatientFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_all_patient, container, false);
    recyclerView = view.findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    databaseReference = FirebaseDatabase.getInstance().getReference("Patients");
    patientAdapter = new PatientAdapter(requireContext(),databaseReference);
    recyclerView.setAdapter(patientAdapter);

        return view;
    }
}