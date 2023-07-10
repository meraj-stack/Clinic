package com.example.clinic;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.DataTruncation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentDoctorFragment extends Fragment {
    private RecyclerView recycler_Appointment;
    private List<Appointment> appointmentList;
    private DoctorAppointmentAdapter doctorAppointmentAdapter;
    private DatabaseReference dref, drpatient;
    private AutoCompleteTextView auto_patientname;
    private List<String> fullnameSuggestion;
    private List<String> namelist;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view =  inflater.inflate(R.layout.fragment_appointment_doctor, container, false);
        recycler_Appointment = view.findViewById(R.id.recycler_Appointments);
        auto_patientname = view.findViewById(R.id.auto_patientname);

        dref = FirebaseDatabase.getInstance().getReference("Appointment");


        appointmentList = new ArrayList<>();
        recycler_Appointment.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctorAppointmentAdapter = new DoctorAppointmentAdapter(requireContext(), appointmentList);
        recycler_Appointment.setAdapter(doctorAppointmentAdapter);

        MainActivity1 mainActivity1 = (MainActivity1) getActivity();
        assert mainActivity1 != null;
        String docfirstname = mainActivity1.getDocFirstName();
        String doclastname = mainActivity1.getDoclastName();


        String doctorname =  "Dr. " + docfirstname + " " + doclastname;

        retreiveAppointments(doctorname);

       /* Query query = dref.orderByChild("drname").equalTo(doctorname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()
                     ) {
                    Appointment appointment = datasnapshot.getValue(Appointment.class);
                    if (appointment!=null){
                        appointmentList.add(appointment);
                    }

                }
                doctorAppointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Coudn't Fetch Data", Toast.LENGTH_SHORT).show();

            }
        });*/

        fullnameSuggestion = new ArrayList<>();
        ArrayAdapter<String> nameadapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, fullnameSuggestion);
        nameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        auto_patientname.setAdapter(nameadapter);


        namelist = new ArrayList<>();


        drpatient = FirebaseDatabase.getInstance().getReference("Patients");

        drpatient.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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



        auto_patientname.setThreshold(1); // Show suggestions after typing one character

        auto_patientname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().toLowerCase().trim();
                List<String> matchingNames = new ArrayList<>();
                for (String fullName : namelist) {
                    if (fullName.toLowerCase().trim().contains(input)) {
                        matchingNames.add(fullName);
                    }
                }
                nameadapter.clear();
                nameadapter.addAll(matchingNames);
                nameadapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        auto_patientname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String patientname = (String) parent.getItemAtPosition(position);

                getFilteredAppointments(patientname, doctorname);
            }
        });




        return view;
    }



    private void getFilteredAppointments(String filteredPatientName, String doctorname){
        appointmentList.clear();
        List<Appointment> filteredAppointment = new ArrayList<>();

        Query query = dref.orderByChild("drname").equalTo(doctorname);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    // Retrieve the appointment data and add it to the RecyclerView adapter
                    // You can modify this part according to your specific appointment data structure
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                   if (appointment.getPateintname().equals(filteredPatientName)){
                       filteredAppointment.add(appointment);
                   }
                }
                // Notify the RecyclerView adapter that the data has changed
                for (Appointment appoint:filteredAppointment
                     ) {
                    appointmentList.add(appoint);
                    
                }
                doctorAppointmentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void retreiveAppointments(String doctorname){
        appointmentList.clear();

        String currentDate = getCurrentDate();
        List<Appointment> filteredAppointment = new ArrayList<>();

        dref.orderByChild("drname").equalTo(doctorname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appointmentsnaps:snapshot.getChildren()
                     ) {
                    Appointment appointment = appointmentsnaps.getValue(Appointment.class);
                    if(appointment.getDate().equals(currentDate)){
                        filteredAppointment.add(appointment);

                    }

                }
                for (Appointment appointments:filteredAppointment
                     ) {
                    appointmentList.add(appointments);

                }
                doctorAppointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "error retrieving data", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}