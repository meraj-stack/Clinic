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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditPatientFragment extends Fragment {
    private EditText edit_fname, edit_lname, edit_dob, edit_sex, edit_marital, edit_address, edit_weight, edit_height, edit_email, edit_mobile, edit_bg, edit_phistory;
    private ImageView img_pProfile;
    private Button btn_savechanges;
    private AlertDialog alertDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_patient, container, false);
        edit_fname = view.findViewById(R.id.edit_fname);
        edit_lname = view.findViewById(R.id.edit_lname);
        edit_email = view.findViewById(R.id.edit_email);
        edit_height = view.findViewById(R.id.edit_height);
        edit_weight = view.findViewById(R.id.edit_weight);
        edit_marital = view.findViewById(R.id.edit_marital);
        edit_dob = view.findViewById(R.id.edit_dob);
        edit_mobile = view.findViewById(R.id.edit_mobile);
        edit_sex = view.findViewById(R.id.edit_sex);
        edit_bg = view.findViewById(R.id.edit_bg);
        edit_address = view.findViewById(R.id.edit_address);
        edit_phistory = view.findViewById(R.id.edit_phistory);
        btn_savechanges = view.findViewById(R.id.btn_savechanges);
        img_pProfile = view.findViewById(R.id.img_pProfile);

        String Uid = getArguments().getString("UID");

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Patients").child(Uid);

        View alertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialogpatient, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertDialogView);
        alertDialog = builder.create();
        Button btn_ok = alertDialogView.findViewById(R.id.btn_ok);


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.getValue(Patient.class);
                edit_fname.setText(patient.getFirstname());
                edit_lname.setText(patient.getLastname());
                edit_weight.setText(String.valueOf(patient.getWeight()));
                edit_height.setText(String.valueOf(patient.getHeight()));
                edit_email.setText(patient.getEmail());
                edit_marital.setText(patient.getMaritalStatus());
                edit_dob.setText(patient.getDob());
                edit_mobile.setText(patient.getMobile());
                edit_sex.setText(patient.getSex());
                edit_bg.setText(patient.getBloodGroup());
                edit_address.setText(patient.getAddress());
                edit_phistory.setText(patient.getPatientHistory());
                Glide.with(requireContext()).load(patient.getProfile()).into(img_pProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Saving Changes...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String firstname = edit_fname.getText().toString();
                String lastname = edit_lname.getText().toString();
                String email = edit_email.getText().toString();
                String mobile = edit_mobile.getText().toString();
                String dob = edit_dob.getText().toString();
                String marital = edit_marital.getText().toString();
                double height = Double.parseDouble(edit_height.getText().toString());
                double weight = Double.parseDouble(edit_weight.getText().toString());
                String add = edit_address.getText().toString();
                String phistory = edit_phistory.getText().toString();
                String bg = edit_bg.getText().toString();
                String sex = edit_sex.getText().toString();

                dbref.child("firstname").setValue(firstname);
                dbref.child("lastname").setValue(lastname);
                dbref.child("sex").setValue(sex);
                dbref.child("dob").setValue(dob);
                dbref.child("mobile").setValue(mobile);
                dbref.child("email").setValue(email);
                dbref.child("weight").setValue(weight);
                dbref.child("height").setValue(height);
                dbref.child("maritalStatus").setValue(marital);
                dbref.child("BloodGroup").setValue(bg);
                dbref.child("patientHistory").setValue(phistory)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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