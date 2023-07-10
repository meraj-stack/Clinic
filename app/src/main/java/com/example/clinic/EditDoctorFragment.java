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

import java.util.Objects;

public class EditDoctorFragment extends Fragment {
   private EditText edit_Fname, edit_lname, edit_Mobile, edit_dob, edit_Bio, edit_City, edit_Sex, edit_Country, edit_Special, edit_Address, edit_Language;
    private Button btn_saveChanges;
    private ImageView img_docProfile;
    private AlertDialog alertDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_edit_doctor, container, false);
        edit_Fname = view.findViewById(R.id.edit_Fname);
        edit_Bio = view.findViewById(R.id.edit_Bio);
        edit_Sex = view.findViewById(R.id.edit_Sex);
        edit_lname = view.findViewById(R.id.edit_Lname);
        edit_Special = view.findViewById(R.id.edit_Special);
        edit_dob = view.findViewById(R.id.edit_Dob);
        edit_Mobile = view.findViewById(R.id.edit_Mobile);
        edit_Language = view.findViewById(R.id.edit_Language);
        edit_Address = view.findViewById(R.id.edit_Address);
        edit_City = view.findViewById(R.id.edit_City);
        edit_Country = view.findViewById(R.id.edit_Country);
        btn_saveChanges = view.findViewById(R.id.btn_saveChanges);
        img_docProfile = view.findViewById(R.id.img_docProfile);
        String Uid = getArguments().getString("Uid");

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("ClinicUser").child(Uid);

        View alertdialogview = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertdialogview);
        alertDialog = builder.create();
        Button btn_ok = alertdialogview.findViewById(R.id.btn_ok);






        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              ClinicUser clinicUser = snapshot.getValue(ClinicUser.class);
              edit_Fname.setText(clinicUser.getFirstname());
              edit_lname.setText(clinicUser.getLastname());
              edit_Mobile.setText(clinicUser.getContact());
              edit_Bio.setText(clinicUser.getBio());
              edit_Special.setText(clinicUser.getSpecialization());
              edit_Sex.setText(clinicUser.getGender());
              edit_Address.setText(clinicUser.getAddress());
              edit_City.setText(clinicUser.getCity());
              edit_Country.setText(clinicUser.getCountry());
              edit_dob.setText(clinicUser.getDob());
              edit_Language.setText(clinicUser.getLanguage());
                Glide.with(requireContext()).load(clinicUser.getImageurl()).into(img_docProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Saving Changes..");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String firstname = edit_Fname.getText().toString();
                String lastname = edit_lname.getText().toString();
                String contact = edit_Mobile.getText().toString();
                String dob = edit_dob.getText().toString();
                String gender = edit_Sex.getText().toString();
                String specialization = edit_Special.getText().toString();
                String city = edit_City.getText().toString();
                String address = edit_Address.getText().toString();
                String country = edit_Country.getText().toString();
                String bio = edit_Bio.getText().toString();
                String language = edit_Language.getText().toString();

                dataref.child("firstname").setValue(firstname);
                dataref.child("lastname").setValue(lastname);
                dataref.child("contact").setValue(contact);
                dataref.child("specialization").setValue(specialization);
                dataref.child("dob").setValue(dob);
                dataref.child("gender").setValue(gender);
                dataref.child("city").setValue(city);
                dataref.child("address").setValue(address);
                dataref.child("bio").setValue(bio);
                dataref.child("country").setValue(country);
                dataref.child("language").setValue(language)
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