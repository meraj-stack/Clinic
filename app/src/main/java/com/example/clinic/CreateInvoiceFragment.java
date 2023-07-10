package com.example.clinic;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateInvoiceFragment extends Fragment {
    private DatabaseReference dr, dinvoice;
    private EditText edit_date, edit_doctorfee;
    private AutoCompleteTextView auto_patienname;
    private Spinner spinner_paymentmode, spinner_paymentstatus, spinner_Doctorname;
    private List<String> fullnameSuggestion;
    private List<String> namelist;
    private Button btn_createInvoice;
    private String patientname;
    private String Date;
    private String paymentmode;
    private String paymentstatus;
    private String doctorfee;
    private String Doctorname;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_invoice, container, false);
        edit_date = view.findViewById(R.id.edit_date);
        edit_doctorfee = view.findViewById(R.id.edit_doctorfees);
        btn_createInvoice = view.findViewById(R.id.btn_createInvoice);
        auto_patienname = view.findViewById(R.id.auto_patientname);
        spinner_paymentstatus = view.findViewById(R.id.spinner_paymentstatus);
        spinner_paymentmode = view.findViewById(R.id.spinner_paymentmode);
        spinner_Doctorname = view.findViewById(R.id.spinner_Doctorname);

        dinvoice  = FirebaseDatabase.getInstance().getReference("Invoice");


        fullnameSuggestion = new ArrayList<>();
        ArrayAdapter<String> nameadapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, fullnameSuggestion);
        nameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        auto_patienname.setAdapter(nameadapter);


        namelist = new ArrayList<>();


        dr = FirebaseDatabase.getInstance().getReference("Patients");

        dr.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        namelist.clear();

                        for (DataSnapshot pateintsnap : dataSnapshot.getChildren()
                        ) {
                            String firstname = pateintsnap.child("firstname").getValue(String.class);
                            String lastname = pateintsnap.child("lastname").getValue(String.class);

                            String fullname = firstname + " " + lastname;
                            namelist.add(fullname);


                        }


                        fullnameSuggestion.addAll(namelist);
                        nameadapter.notifyDataSetChanged();


                    }
                } else {
                    Exception exception = task.getException();
                    Toast.makeText(requireContext(), "error retreiving names" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayAdapter<String> adapter_paymentmode = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new String[] {"Chosse Payment mode", "Cash", "Cheque", "UPI"});
        adapter_paymentmode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_paymentmode.setAdapter(adapter_paymentmode);
        spinner_paymentmode.setSelection(0);

        ArrayAdapter<String> adapter_paymentstatus = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new String[] {"Payment Status", "Paid", "Unpaid"});
        adapter_paymentstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_paymentstatus.setAdapter(adapter_paymentstatus);
        spinner_paymentstatus.setSelection(0);

        String date = getCurrentDate();
        edit_date.setText(date);


        DatabaseReference drdoctor = FirebaseDatabase.getInstance().getReference().child("ClinicUser");
        Query query = drdoctor.orderByChild("usertype").equalTo("Doctor");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> drnamelist = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String firstname = dataSnapshot.child("firstname").getValue(String.class);
                    String lastname = dataSnapshot.child("lastname").getValue(String.class);
                    String drfullname = "Dr. " + firstname + " " + lastname;
                    drnamelist.add(drfullname);
                }

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, drnamelist);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_Doctorname.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        auto_patienname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patientname = (String) parent.getItemAtPosition(position);

            }
        });

        btn_createInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               doctorfee = edit_doctorfee.getText().toString();
                Date = edit_date.getText().toString();
                paymentmode = spinner_paymentmode.getSelectedItem().toString();
                paymentstatus = spinner_paymentstatus.getSelectedItem().toString();
                Doctorname = spinner_Doctorname.getSelectedItem().toString();

                if(patientname!=null && Date!=null && paymentstatus!=null && paymentmode!=null && doctorfee!=null){
                    CreateInvoice createInvoice = new CreateInvoice(patientname, doctorfee, Doctorname, paymentmode, paymentstatus, Date);
                    String key = dinvoice.push().getKey();
                    dinvoice.child(key).setValue(createInvoice).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(requireContext(), "Invoice Created Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Can't Create Invoice", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });




        return view;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}