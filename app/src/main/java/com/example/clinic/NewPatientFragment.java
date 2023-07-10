package com.example.clinic;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class NewPatientFragment extends Fragment {
    private CircularImageView patient_profile;
    private TextView txt_upload;
    private EditText edit_Firstname, edit_Lastname, edit_Mobile, edit_Email, edit_Dob, edit_Weight, edit_Height, edit_Address, edit_History, edit_Diseases, edit_Knowndisease;
    private Spinner spinner_sex, spinner_history, spinner_status, spinner_blood;
    private Button btn_save;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri selectedImage = null;
    private Bitmap imagebitmap = null;

    private Bitmap capturedImage = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_patient, container, false);
        edit_Firstname = view.findViewById(R.id.edit_Firstname);
        edit_Lastname = view.findViewById(R.id.edit_Lastname);
        edit_History = view.findViewById(R.id.edit_History);
        edit_Mobile = view.findViewById(R.id.edit_Mobile);
        edit_Email = view.findViewById(R.id.edit_Email);
        edit_Dob = view.findViewById(R.id.edit_Dob);
        edit_Weight = view.findViewById(R.id.edit_Weight);
        edit_Height = view.findViewById(R.id.edit_Hieght);
        edit_Address = view.findViewById(R.id.edit_Address);
        edit_Knowndisease = view.findViewById(R.id.edit_KnownDiseases);
        edit_Diseases = view.findViewById(R.id.edit_diseases);
        spinner_sex = view.findViewById(R.id.spinner_sex);
        spinner_status = view.findViewById(R.id.spinner_status);
        spinner_history = view.findViewById(R.id.spinner_history);
        spinner_blood = view.findViewById(R.id.spinner_Blood);
        patient_profile = view.findViewById(R.id.patient_profile);
        txt_upload = view.findViewById(R.id.txt_upload);
        btn_save = view.findViewById(R.id.btn_save);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), R.layout.spinner_text, new String[]{"Gender","Male", "Female", "Transgender"});
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_sex.setAdapter(adapter1);
        spinner_sex.setSelection(0);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.spinner_text, new String[]{"Blood Group","A+", "A-", "B+","B-","O+", "O-", "AB+", "AB-"});
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_blood.setAdapter(adapter2);
        spinner_blood.setSelection(0);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), R.layout.spinner_text, new String[]{"Marital status","Married", "Single"});
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_status.setAdapter(adapter3);
        spinner_status.setSelection(0);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(requireContext(), R.layout.spinner_text, new String[]{"Family History", "Mother", "Father", "Brother", "Sister"});
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_history.setAdapter(adapter4);
        spinner_history.setSelection(0);


        txt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Choose an Option");
                builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraLauncher.launch(cameraintent);
                                break;
                            case 1:
                                Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryLauncher.launch(galleryintent);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });



        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()==RESULT_OK){
                        assert result.getData() != null;
                        selectedImage = result.getData().getData();
                        patient_profile.setImageURI(selectedImage);
                    }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        if (result.getData().getExtras().get("data") != null) {
                            capturedImage = (Bitmap) result.getData().getExtras().get("data");
                            patient_profile.setImageBitmap(capturedImage);
                        }
                    }
                });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = edit_Firstname.getText().toString();
                String lastname = edit_Lastname.getText().toString();
                String mobile = edit_Mobile.getText().toString();
                String email = edit_Email.getText().toString();
                String dob = edit_Dob.getText().toString();
                Double weight = Double.parseDouble(edit_Weight.getText().toString());
                Double height = Double.parseDouble(edit_Height.getText().toString());
                String address = edit_Address.getText().toString();
                String patientHis = edit_History.getText().toString();
                String bg = spinner_blood.getSelectedItem().toString();
                String sex = spinner_sex.getSelectedItem().toString();
                String status = spinner_status.getSelectedItem().toString();

                if(selectedImage!=null || capturedImage!=null){
                    imagebitmap = capturedImage;
                    if(imagebitmap==null){
                        try {
                            imagebitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Please Select a Profile Image", Toast.LENGTH_SHORT).show();
                }

                String filename = UUID.randomUUID().toString() + ".jpg";
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("patientProfile/" + filename);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte [] imagedata = baos.toByteArray();
                UploadTask uploadTask = ref.putBytes(imagedata);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profileImage = uri.toString();
                                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Patients");
                                    Patient patient = new Patient(firstname, lastname, mobile, email, dob, sex,bg, status, height, weight, address, patientHis, profileImage);
                                    String patientId = dr.push().getKey();
                                    dr.child(patientId).setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(requireContext(), "Data Successfully Saved", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(), "Failed to save Data", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), "Image Url can't Be Downloaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Upload Task Failed", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });


        return view;
    }
}