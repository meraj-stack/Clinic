package com.example.clinic;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private EditText edit_datePicker;
    private Button btn_nameSearch, btn_Apply;
    private AutoCompleteTextView auto_nameSearch;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private boolean isStartDateSelected = false;
    private DatabaseReference dinvoice;
    private List<CreateInvoice> reportlist;
    private ReportsAdapter reportsAdapter;
    private RecyclerView recycler_reports;
    private TextView txt_noofrows, txt_totalamount, txt_exportpdf;
    private int numberOfRows = 0;





    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_report, container, false);
        btn_Apply = view.findViewById(R.id.btn_Apply);
        edit_datePicker = view.findViewById(R.id.edit_datePicker);
        recycler_reports = view.findViewById(R.id.recycler_reports);
        txt_noofrows = view.findViewById(R.id.txt_noofrows);
        txt_totalamount= view.findViewById(R.id.txt_totalamount);
        auto_nameSearch = view.findViewById(R.id.auto_nameSearch);
        txt_exportpdf = view.findViewById(R.id.txt_exportpdf);




        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        dinvoice  = FirebaseDatabase.getInstance().getReference("Invoice");

        edit_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateSelected = true;
                showFilterDatePickerDialog();

            }
        });

        btn_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyDates(v);
            }
        });
        dinvoice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> doctorNames = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming doctor names are stored as a child value in the database
                    CreateInvoice invoice = snapshot.getValue(CreateInvoice.class);
                    assert invoice != null;
                    String doctorName = invoice.getDoctorname();
                    doctorNames.add(doctorName);
                }

                // Set up the ArrayAdapter for the AutocompleteTextView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, doctorNames);
                auto_nameSearch.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if necessary
            }
        });

        auto_nameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();
                Query query = dinvoice.orderByChild("doctorname").startAt(searchText).endAt(searchText + "\uf8ff");


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reportlist = new ArrayList<>();
                        reportlist.clear();
                        double totalamount = 0.0;
                        numberOfRows = 0;


                        for (DataSnapshot datasnapshot: snapshot.getChildren()
                             ) {
                            CreateInvoice Invoice = datasnapshot.getValue(CreateInvoice.class);
                            if(Invoice!=null){
                                reportlist.add(Invoice);
                                double billingamount = Double.parseDouble(Invoice.getDoctorfee());
                                totalamount+=billingamount;
                                numberOfRows++;


                            }

                        }
                        recycler_reports.setLayoutManager(new LinearLayoutManager(requireContext()));
                        reportsAdapter = new ReportsAdapter(requireContext(), reportlist);
                        recycler_reports.setAdapter(reportsAdapter);
                        reportsAdapter.notifyDataSetChanged();

                        txt_totalamount.setText(String.valueOf(totalamount));
                        txt_noofrows.setText(String.valueOf(numberOfRows));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        txt_exportpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = edit_datePicker.getText().toString();
                String[] fulldate = date.split(" - ");
                String startdate = fulldate[0];
                String enddate = fulldate[1];
                createPDF(startdate, enddate);

            }
        });


        return view;
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
        edit_datePicker.setText(dates);
    }
    private void applyDateRangeFilter(Date startDate, Date endDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);
        dinvoice.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportlist = new ArrayList<>();
                reportlist.clear();
                double totalBillingAmount = 0.0;
                for (DataSnapshot datasnap : snapshot.getChildren()
                ) {
                    CreateInvoice createInvoice = datasnap.getValue(CreateInvoice.class);
                    if (createInvoice != null) {
                        reportlist.add(createInvoice);
                        double billingamount = Double.parseDouble(createInvoice.getDoctorfee());
                        totalBillingAmount += billingamount;
                        numberOfRows++;

                    }

                }
                recycler_reports.setLayoutManager(new LinearLayoutManager(getContext()));
                reportsAdapter = new ReportsAdapter(requireContext(), reportlist);
                recycler_reports.setAdapter(reportsAdapter);

                reportsAdapter.notifyDataSetChanged();

                txt_noofrows.setText(String.valueOf(numberOfRows));
                txt_totalamount.setText(String.valueOf(totalBillingAmount));
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
    private void createPDF(String startDate, String endDate) {
        // Create a new document
        Document document = new Document();

        // Get the directory to save the PDF
        String pdfFileName = "InvoiceReport.pdf";
        File pdfFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), pdfFileName);

        try {
            // Create a PdfWriter instance to write the document to a file
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            // Open the document
            document.open();

            // Add a heading
            Paragraph heading = new Paragraph("Report.pdf");
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            // Add subheadings for start and end dates
            Paragraph startDateParagraph = new Paragraph("From: " + startDate);
            startDateParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(startDateParagraph);

            Paragraph endDateParagraph = new Paragraph("To: " + endDate);
            endDateParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(endDateParagraph);

            // Add content to the document (e.g., the filtered rows)
            // Example: Iterate over the reportlist and add each row as a paragraph
            for (CreateInvoice invoice : reportlist) {
                Paragraph paragraph = new Paragraph(invoice.toString());
                document.add(paragraph);
            }

            // Close the document
            document.close();

            Toast.makeText(requireContext(), "pdf exported successfully", Toast.LENGTH_SHORT).show();


        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();

            // Handle any exceptions that occur during PDF creation
        }
    }

}