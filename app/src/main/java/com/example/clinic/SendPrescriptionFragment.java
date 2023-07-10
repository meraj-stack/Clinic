package com.example.clinic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendPrescriptionFragment extends Fragment {
    private TextView txt_druginfo, txt_testinfo, txt_name, txt_doctorname, txt_descipline, txt_date;
    private String email ="";
    private Button btn_sendpdf;
    private LinearLayout linearLayout;
    private EditText edit_docsign;
    NavigationView navigationView;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_prescription, container, false);
        txt_druginfo = view.findViewById(R.id.txt_druginfo);
        txt_testinfo = view.findViewById(R.id.txt_testinfo);
        txt_name = view.findViewById(R.id.txt_name);
        txt_doctorname = view.findViewById(R.id.txt_doctorname);
        txt_date = view.findViewById(R.id.txt_date);
        txt_descipline = view.findViewById(R.id.txt_descipline);
        btn_sendpdf = view.findViewById(R.id.btn_sendpdf);
        linearLayout = view.findViewById(R.id.linearlayout);
        edit_docsign = view.findViewById(R.id.edit_docsign);


        MainActivity1 mainActivity1 = (MainActivity1)getActivity();
        assert mainActivity1 != null;
        String docfirstname = mainActivity1.getDocFirstName();
        String doclastname = mainActivity1.getDoclastName();
        String descipline = mainActivity1.getSpecialization();


        txt_doctorname.setText("Dr. " + docfirstname + " " + doclastname);
        txt_descipline.setText(descipline);
        edit_docsign.setText(docfirstname + " " + doclastname + ".");

        setCurrentDate();











        Bundle bundle = getArguments();
        if (bundle != null) {
            String drugs = bundle.getString("drugs");
            String tests = bundle.getString("tests");
            String name = bundle.getString("name");
            if (drugs != null && tests != null && name != null) {
                txt_druginfo.setText(drugs);
                txt_testinfo.setText(tests);
                txt_name.setText(name);
            }


        }





        String fullname = txt_name.getText().toString();
        String[] nameParts = fullname.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        btn_sendpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailFromDatabase(firstName, lastName);





            }
        });

        return view;
    }

    private void getEmailFromDatabase(String firstname, String lastname) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Patients");
        dbref.orderByChild("firstname").equalTo(firstname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String storedlastname = dataSnapshot.child("lastname").getValue(String.class);
                    if (storedlastname != null && storedlastname.equals(lastname)) {
                        email = dataSnapshot.child("email").getValue(String.class);
                        break;
                    }

                }
                if (email != null) {
                    Toast.makeText(requireContext(), email, Toast.LENGTH_SHORT).show();
                    generateAndSharePDF(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "coundn't fetch email", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private File generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(linearLayout.getWidth(), linearLayout.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        linearLayout.draw(canvas);

        pdfDocument.finishPage(page);

        File directory = new File(requireContext().getExternalFilesDir(null), "YourPDFs");
        if (!directory.exists()) {
            directory.mkdirs();
        }


        String fileName = "prescription_file.pdf";
        File file = new File(directory, fileName);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(requireContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to generate PDF", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            pdfDocument.close();
        }


    }

    public void sharePDF(File file, String email) {


        if (file != null) {
            // Proceed with sharing the PDF
            Uri pdfUri = FileProvider.getUriForFile(requireContext(), "com.example.clinic.fileprovider", file);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Set recipient's email address (change to your actual email address)

            shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});


            // Check if Gmail is available
            Intent gmailIntent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
            if (gmailIntent != null) {
                gmailIntent.setPackage("com.google.android.gm");


                Intent chooserIntent = Intent.createChooser(shareIntent, "Share PDF using");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{gmailIntent});
                startActivity(chooserIntent);
            } else {
                // If Gmail is not available, fallback to regular share
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share PDF using");
                if (shareIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(chooserIntent);
                } else {
                    Toast.makeText(requireContext(), "No app available to share PDF", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(requireContext(), "PDF generation failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void generateAndSharePDF(String Email){
        File pdffile = generatePDF();
        if(pdffile!=null){
            sharePDF(pdffile, Email);
        }else {
            Toast.makeText(requireContext(), "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrentDate(){
        Date currentdate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formateddate = dateFormat.format(currentdate);

        txt_date.setText(formateddate);
    }
}

