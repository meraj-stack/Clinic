package com.example.clinic;

import android.annotation.SuppressLint;
import android.hardware.lights.LightsManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewPrescriptionFragment extends Fragment {
    private EditText ed_advice, ed_duration, ed_quantity, ed_description, ed_quantity2;
    private Spinner spinner_dose, spinner_drugname, spinner_pateintname, spinner_testname;
    private Button btn_adddrug, btn_addtest, btn_createpres;
    private TextView txt_display, txt_pateintname, txt_testlist;
    private StringBuilder prescription, testlist;
    private   Fragment fragment = null;
    private String  firstname = null;
    private String lastname = null;
    private   int sequenceNumber = 1;
    private   int Number = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_prescription, container, false);
        spinner_drugname = view.findViewById(R.id.spinner_drugname);
        spinner_pateintname = view.findViewById(R.id.spinner_patientname);
        spinner_dose = view.findViewById(R.id.spinner_dose);
        spinner_testname = view.findViewById(R.id.spinner_testname);
        ed_advice = view.findViewById(R.id.ed_advice);
        ed_duration = view.findViewById(R.id.ed_duration);
        ed_description = view.findViewById(R.id.ed_description);
        ed_quantity = view.findViewById(R.id.ed_quantity);
        ed_quantity2 = view.findViewById(R.id.ed_quantity2);
        btn_adddrug = view.findViewById(R.id.btn_adddrug);
        btn_createpres = view.findViewById(R.id.btn_createpres);
        btn_addtest = view.findViewById(R.id.btn_addtest);
        txt_display = view.findViewById(R.id.txt_display);
        txt_pateintname = view.findViewById(R.id.txt_pateintname);
        txt_testlist = view.findViewById(R.id.txt_testlist);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Patients");

        dref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot!=null){
                        List<String> namelist = new ArrayList<>();

                        for (DataSnapshot pateintsnap:dataSnapshot.getChildren()
                             ) {
                         firstname = pateintsnap.child("firstname").getValue(String.class);
                          lastname = pateintsnap.child("lastname").getValue(String.class);

                            String fullname = firstname + " " + lastname;
                            namelist.add(fullname);


                        }
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, namelist);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_pateintname.setAdapter(adapter1);
                    }
                }else {
                    Exception exception = task.getException();
                    Toast.makeText(requireContext(), "error retreiving names" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        DatabaseReference ddrugs = FirebaseDatabase.getInstance().getReference("Drugs");
        ddrugs.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();

                    if (dataSnapshot != null) {
                        List<String> druglist = new ArrayList<>();

                        for (DataSnapshot drugsnap : dataSnapshot.getChildren()
                        ) {
                            String tradename = drugsnap.child("tradename").getValue(String.class);
                            druglist.add(tradename);

                        }

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, druglist);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_drugname.setAdapter(adapter2);
                    }
                }
                else {
                    Exception exception = task.getException();
                    Toast.makeText(requireContext(), "error retreiving drugs" + exception.getMessage(), Toast.LENGTH_SHORT).show();

                }
                
            }
        });

        DatabaseReference dtest = FirebaseDatabase.getInstance().getReference("Tests");

        dtest.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    if(dataSnapshot!=null){
                        List<String> testlist = new ArrayList<>();

                        for (DataSnapshot testsnap:dataSnapshot.getChildren()
                             ) {
                            String testname = testsnap.child("testname").getValue(String.class);
                            testlist.add(testname);

                        }

                        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, testlist);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_testname.setAdapter(adapter3);
                    }
                }
                else {
                    Exception exception = task.getException();
                    Toast.makeText(requireContext(), "error retreiving tests" + exception.getMessage(), Toast.LENGTH_SHORT).show();


                }
            }
        });

        prescription = new StringBuilder();

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[] {"po","bd", "tds","od","null"});
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dose.setAdapter(adapter4);



        ed_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String durationString = s.toString();
                String selectedItem = spinner_dose.getSelectedItem().toString();
                if (selectedItem!=null) {
                    double quantity = calculateQuantity(selectedItem, durationString);
                    ed_quantity2.setText(String.valueOf(quantity));
                }else{
                    Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_adddrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String duration = ed_duration.getText().toString();
                String quantity = ed_quantity.getText().toString();
                String advice = ed_advice.getText().toString();
                String name = spinner_pateintname.getSelectedItem().toString();
                String dose = spinner_dose.getSelectedItem().toString();
                String drugname = spinner_drugname.getSelectedItem().toString();


                String druginfo = sequenceNumber + ".Drug name : " + drugname
                                  + "\n   Dose : " + dose
                                  + "\n   Quantity : " + quantity
                                  + "\n   Duration : " + duration
                                  + "\n   Advice : " + advice
                                  + "\n\n";

                sequenceNumber++;
                txt_pateintname.setText(name);
                prescription.append(druginfo);

                txt_display.setText(prescription.toString());

                ed_quantity.setText("");
                ed_advice.setText("");
                ed_duration.setText("");
                txt_display.setVisibility(View.VISIBLE);



            }
        });

        testlist = new StringBuilder();

        btn_addtest.setOnClickListener(new View.OnClickListener() {
            @Override

















            public void onClick(View v) {
                String test = spinner_testname.getSelectedItem().toString();
                String description = ed_description.getText().toString();

                String testinfo = Number + ".Test : " + test
                                  + "\n   Description : " + description
                                  + "\n\n";
                Number++;
                testlist.append(testinfo);
                txt_testlist.setText(testlist.toString());
                ed_description.setText("");
                txt_testlist.setVisibility(View.VISIBLE);


            }
        });
        btn_createpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("drugs", getDrugInfo());
                bundle.putString("tests", getTestList());
                bundle.putString("name", txt_pateintname.getText().toString());


                fragment = new SendPrescriptionFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });







        return view;
    }

    public String getDrugInfo(){
        return txt_display.getText().toString();
    }

    public String getTestList(){
        return txt_testlist.getText().toString();
    }

    private double calculateQuantity(String selecteditem, String durationstring){
        int duration = 0;


        try {
            duration = Integer.parseInt(durationstring);
        } catch (NumberFormatException e) {

        }

        double quantity = 0.0;
        if (selecteditem.equals("bd")) {
            quantity = 2 * duration; // Replace "bd" with the appropriate value for bd
        } else if (selecteditem.equals("tds")) {
            quantity = 3 * duration; // Replace "tds" with the appropriate value for tds
        } else if (selecteditem.equals("od")) {
            quantity = duration; // Replace "od" with the appropriate value for od
        } else if (selecteditem.equals("null")) {

            quantity = 0.0; // Replace "null" with the appropriate value for null
        }

        return quantity;

    }
}