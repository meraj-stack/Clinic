package com.example.clinic;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.Manifest;

public class AppointmentFragment extends Fragment {
    private Spinner  spinner_doctorname, spinner_time;
    private EditText edit_date;
    private Calendar calendar;
    private Button btn_appointment, btn_apply, btn_search;
    private RecyclerView recycler_appointment;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private AutoCompleteTextView auto_name, auto_namecity;
    private List<String> fullnameSuggestion;
    List<String> namelist;
    private List<String> addresslist = new ArrayList<>();
    private String patientname = "";
    private EditText edit_datepicker;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private boolean isStartDateSelected = false;
    private DatabaseReference dref;
    private DatabaseReference drpatient;
    private ProgressBar  mProgressbar;

    private String selectedText;
    private String mobilenumber;
    private  String drname;
    private String date;
    private String time;

    private TextView txt_sendwhatsapp;

    private static final String TWILIO_ACCOUNT_SID = "AC96061846d59538f5c1fb565015fbab04";
    private static final String TWILIO_AUTH_TOKEN = "65cb9ff32017b9b1c25b6229022c6ec7";
    private static final String TWILIO_PHONE_NUMBER = "+14027726436";

    private ActivityResultLauncher<String> permissionLauncher;
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        spinner_doctorname = view.findViewById(R.id.spinner_doctorname);
        auto_name = view.findViewById(R.id.auto_name);
        auto_namecity = view.findViewById(R.id.auto_namesearch);
        spinner_time = view.findViewById(R.id.spinner_time);
        edit_date = view.findViewById(R.id.edit_date);
        edit_datepicker = view.findViewById(R.id.edit_datepicker);
        btn_appointment = view.findViewById(R.id.btn_appointment);
        btn_apply = view.findViewById(R.id.btn_apply);
        btn_search = view.findViewById(R.id.btn_search);
        txt_sendwhatsapp = view.findViewById(R.id.txt_sendwhatsapp);
        recycler_appointment = view.findViewById(R.id.recycler_appointment);
        mProgressbar = view.findViewById(R.id.progressbar);
        calendar = Calendar.getInstance();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();


        appointmentList = new ArrayList<>();
        recycler_appointment.setLayoutManager(new LinearLayoutManager(requireContext()));
        appointmentAdapter = new AppointmentAdapter(requireContext(), appointmentList);
        recycler_appointment.setAdapter(appointmentAdapter);

        dref = FirebaseDatabase.getInstance().getReference("Appointment");

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        edit_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateSelected = true;
                showFilterDatePickerDialog();

            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressbar.setVisibility(View.VISIBLE);
                mProgressbar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7366FF")));
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    int progress = 0;
                    @Override
                    public void run() {
                        mProgressbar.setProgress(progress);
                        progress+=10;
                        if(progress<mProgressbar.getMax()){
                            handler.postDelayed(this, 100);
                        }else{
                            mProgressbar.setVisibility(View.GONE);
                            applyDates(v);
                        }
                    }
                };
                handler.postDelayed(runnable, 100);


            }
        });


        fullnameSuggestion = new ArrayList<>();
        ArrayAdapter<String> nameadapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, fullnameSuggestion);
        nameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        auto_name.setAdapter(nameadapter);


        namelist = new ArrayList<>();

        /*auto_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().toLowerCase().trim();
                List<String> matchingNames = new ArrayList<>();
                for (String fullName: namelist
                ) {
                    if(fullName.toLowerCase().trim().contains(input)){
                        matchingNames.add(fullName);
                    }

                }
                fullnameSuggestion.clear();
                fullnameSuggestion.addAll(matchingNames);
                nameadapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


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

        drpatient.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        addresslist.clear();

                        for (DataSnapshot patientsnap : dataSnapshot.getChildren()) {
                            String address = patientsnap.child("address").getValue(String.class);
                            if (address != null) {
                                addresslist.add(address);
                            }
                        }

                    }

                } else {
                    Exception exception = task.getException();
                    Toast.makeText(requireContext(), "Error retrieving addresses: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        auto_name.setThreshold(1); // Show suggestions after typing one character

        auto_name.addTextChangedListener(new TextWatcher() {
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

        auto_namecity.setThreshold(1);

        auto_namecity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input2 = s.toString().toLowerCase().trim();

                if (isInputMatchingName(input2)) {
                    // Input matches name, suggest full names
                    List<String> matchingNames = getMatchingNames(input2);
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, matchingNames);
                    auto_namecity.setAdapter(nameAdapter);
                } else {
                    // Input matches address, suggest addresses
                    List<String> matchingAddresses = getMatchingAddresses(input2);
                    ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, matchingAddresses);
                    auto_namecity.setAdapter(addressAdapter);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
                spinner_doctorname.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"10:00-10:30 AM", "10:30-11:00 AM", "11:00-11:30 AM", "18:00-18:30 PM", "18:30-19:00 PM", "19:00-19:30 PM", "19:30-20:00 PM"});
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter3);

        auto_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patientname = (String) parent.getItemAtPosition(position);

            }
        });
        auto_namecity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedText = (String) parent.getItemAtPosition(position);
                filterRecyclerViewData(selectedText);


            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressbar.setVisibility(View.VISIBLE);
                mProgressbar.getProgressDrawable().setColorFilter(Color.parseColor("#7366FF"), PorterDuff.Mode.SRC_IN);

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    int progress = 0;
                    @Override
                    public void run() {
                        mProgressbar.setProgress(progress);
                        progress+=10;
                        if(progress<mProgressbar.getMax()){
                            handler.postDelayed(this, 100);
                        }else{
                            mProgressbar.setVisibility(View.GONE);
                            String userInput = auto_namecity.getText().toString();

                            if (isInputValid(userInput)) {
                                if (isInputMobileNumber(userInput)) {
                                    filterByMobileNo(userInput);
                                } else {
                                    Toast.makeText(requireContext(), "Please select an address from the list", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };
                handler.postDelayed(runnable, 100);



            }
        });


        btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "In Progress..";
                 drname = spinner_doctorname.getSelectedItem().toString();
                 date = edit_date.getText().toString();
                 time = spinner_time.getSelectedItem().toString();


                if (patientname != null && drname != null && date != null && time != null) {
                    Appointment appointment = new Appointment(patientname, drname, time, date, status);
                    String key = dref.push().getKey();
                    dref.child(key).setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            String[] fullNameParts = patientname.split(" ");
                            String firstName = fullNameParts[0];
                            String lastName = fullNameParts[1];
                            Query query = drpatient.orderByChild("firstname").equalTo(firstName);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot datasnapshot: snapshot.getChildren()
                                    ) {
                                        Patient patient = datasnapshot.getValue(Patient.class);
                                        if (patient!=null && patient.getLastname().equals(lastName)){
                                             mobilenumber = patient.getMobile();
                                        }

                                    }
                                    if (mobilenumber != null) {
                                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                            // Permission is not granted, request it
                                            permissionLauncher.launch(Manifest.permission.SEND_SMS);
                                        } else {
                                            // Permission is already granted, proceed with sending the SMS
                                            sendSMS(mobilenumber, patientname, drname, date, time);
                                            Toast.makeText(requireContext(), "Appointment Booked Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Mobile number not found", Toast.LENGTH_SHORT).show();


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(requireContext(), "can't retreive mobile number", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "can't Fix appointment", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Toast.makeText(requireContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean isGranted) {
                if (isGranted) {
                    // Permission is granted, proceed with sending the SMS
                    sendSMS(mobilenumber, patientname, drname, date, time);
                } else {
                    // Permission is denied, handle this situation gracefully (e.g., show a message to the user)
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_sendwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hi, " + patientname + ", your appointment is confirmed with " + drname + " on " + date + " between " + time;
                String phonenumber = "+91" + mobilenumber;
                sendWhatsapp(phonenumber, message);



            }
        });





        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointment.setId(dataSnapshot.getKey());
                        appointmentList.add(appointment);
                    }

                }
                appointmentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "cannot fetch data", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Set the selected date in the EditText
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditTextDate(); // Call the method here
            }
        };

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateEditTextDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edit_date.setText(dateFormat.format(calendar.getTime()));
    }

    public void showFilterDatePickerDialog() {


        Calendar calendar1;
        if (isStartDateSelected) {
            calendar1 = startCalendar;
        } else {
            calendar1 = endCalendar;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, monthOfYear);
                    calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (isStartDateSelected) {
                        isStartDateSelected = false;
                        showFilterDatePickerDialog();
                    } else {
                        updateEditText();
                    }
                },
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }


    private void updateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = dateFormat.format(startCalendar.getTime());
        String endDate = dateFormat.format(endCalendar.getTime());
        String dates = startDate + " - " + endDate;
        edit_datepicker.setText(dates);
    }

    private void applyDateRangeFilter(Date startDate, Date endDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);
        dref.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot datasnap : snapshot.getChildren()
                ) {
                    Appointment appointment = datasnap.getValue(Appointment.class);
                    if (appointment != null) {
                        appointment.setId(datasnap.getKey());
                        appointmentList.add(appointment);
                    }

                }
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void applyDates(View view) {
        // Get the selected start and end dates from the Calendar instances
        Date startDate = startCalendar.getTime();
        Date endDate = endCalendar.getTime();

        // Call the method to fetch filtered data from Firebase within the selected date range
        applyDateRangeFilter(startDate, endDate);
    }

    private boolean isInputMatchingName(String input) {
        for (String fullname : namelist
        ) {
            String firstname = fullname.split(" ")[0];
            String lastname = fullname.split(" ")[1];

            if (firstname.toLowerCase().startsWith(input) || lastname.toLowerCase().startsWith(input)) {
                return true;
            }

        }
        return false;
    }

    private List<String> getMatchingNames(String input) {
        List<String> matchingNames = new ArrayList<>();
        for (String fullname : namelist) {
            String firstname = fullname.split(" ")[0];
            String lastname = fullname.split(" ")[1];
            if (firstname.toLowerCase().startsWith(input) || lastname.toLowerCase().startsWith(input)) {
                matchingNames.add(fullname);
            }
        }
        return matchingNames;
    }

    private List<String> getMatchingAddresses(String input) {
        List<String> matchingAddresses = new ArrayList<>();
        for (String address : addresslist) {
            if (address.toLowerCase().contains(input)) {
                matchingAddresses.add(address);
            }
        }
        return matchingAddresses;
    }

    private void filterRecyclerViewData(String selectedAddress) {
        appointmentList.clear();
        Query query = drpatient.orderByChild("address").equalTo(selectedAddress);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot patientSnapshot : snapshot.getChildren()) {
                    String patientName = patientSnapshot.child("firstname").getValue(String.class)
                            + " " + patientSnapshot.child("lastname").getValue(String.class);
                    // Retrieve the appointments associated with the filtered patient names
                    getFilteredAppointments(patientName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFilteredAppointments(String filteredPatientName) {

        Query query = dref.orderByChild("pateintname").equalTo(filteredPatientName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    // Retrieve the appointment data and add it to the RecyclerView adapter
                    // You can modify this part according to your specific appointment data structure
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    appointmentList.add(appointment);
                }
                // Notify the RecyclerView adapter that the data has changed
                appointmentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filterByMobileNo(String mobile) {
        appointmentList.clear();
        Query query = drpatient.orderByChild("mobile").equalTo(mobile);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datasnapshot : snapshot.getChildren()
                    ) {
                        String name = datasnapshot.child("firstname").getValue(String.class) + " " + datasnapshot.child("lastname").getValue(String.class);
                        Query appointmentQuery = dref.orderByChild("pateintname").equalTo(name);
                        appointmentQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                                    appointmentList.add(appointment);
                                    assert appointment != null;
                                    appointment.setId(appointmentSnapshot.getKey());
                                }
                                appointmentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(requireContext(), "No match found", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }else {
                    Toast.makeText(requireContext(), "No match found", Toast.LENGTH_SHORT).show();

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "No match found", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isInputValid(String input) {
        // You can add your validation logic here
        // For example, check if the input is not empty or meets certain criteria
        return !TextUtils.isEmpty(input);
    }

    private boolean isInputMobileNumber(String input) {
        // Check if the input consists of only digits
        return input.matches("\\d+");
    }

    private void sendSMS(String phoneNumber,String patientName, String doctorName, String appointmentDate, String Time) {

        String message = "Hi, " + patientName + ", your appointment is confirmed with " + doctorName + " on " + appointmentDate + " between " + Time;

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<>();

        // Create a PendingIntent for each message part
        for (int i = 0; i < parts.size(); i++) {
            Intent sentIntent = new Intent("SMS_SENT");
            PendingIntent sentPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, sentIntent, PendingIntent.FLAG_IMMUTABLE);
            sentIntents.add(sentPendingIntent);
        }

        // Send the SMS message
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, null);
    }

    private void sendWhatsapp(String patientnumber, String message){
        PackageManager packageManager = requireActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + patientnumber + "&text=" + Uri.encode(message)));
        if (intent.resolveActivity(packageManager)!=null){
            startActivity(intent);

        }
        else {
            Toast.makeText(requireContext(), "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
        }



    }


}

