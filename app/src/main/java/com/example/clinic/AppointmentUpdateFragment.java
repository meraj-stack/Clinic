package com.example.clinic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AppointmentUpdateFragment extends Fragment {
    private EditText edit_pname, edit_dname, edit_time, edit_date;
    private Spinner spinner_updatestatus;
    private AlertDialog alertDialog;
    private Button btn_update;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_update, container, false);

        edit_pname = view.findViewById(R.id.edit_pname);
        edit_dname = view.findViewById(R.id.edit_dname);
        edit_time = view.findViewById(R.id.edit_time);
        edit_date = view.findViewById(R.id.edit_date);
        btn_update = view.findViewById(R.id.btn_update);
        spinner_updatestatus = view.findViewById(R.id.spinner_updatestatus);
        assert getArguments() != null;
        String key = getArguments().getString("key");

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Appointment").child(key);

        View alertdialogview = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialogpatient, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertdialogview);
        alertDialog = builder.create();
        Button btn_ok = alertdialogview.findViewById(R.id.btn_ok);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"In Progress..", "Completed", "Cancelled"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_updatestatus.setAdapter(adapter);

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Appointment appointment = snapshot.getValue(Appointment.class);
                edit_pname.setText(appointment.getPateintname());
                edit_dname.setText(appointment.getDrname());
                edit_date.setText(appointment.getDate());
                edit_time.setText(appointment.getTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Saving Changes..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String time = edit_time.getText().toString();
                String date = edit_date.getText().toString();
                String status = spinner_updatestatus.getSelectedItem().toString();

                dr.child("time").setValue(time);
                dr.child("date").setValue(date);
                dr.child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        alertDialog.show();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        return view;
    }
}