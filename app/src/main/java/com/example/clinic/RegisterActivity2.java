package com.example.clinic;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class RegisterActivity2 extends AppCompatActivity {
    private Spinner spinner_language, spinner_gender, spinner_specialization, spinner_usertype;
    private EditText ed_firstname, ed_lastname, ed_dob, ed_contact, ed_address, ed_city, ed_country, ed_username, ed_password, ed_bio;
    private TextView txt_uploadimage;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private CircularImageView image;
    private Bitmap capturedImage = null;
    private Uri selectedImage = null;
    private Bitmap imagebitmap = null;
    private Button btn_register;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_language = findViewById(R.id.spinner_language);
        spinner_specialization = findViewById(R.id.spinner_specialization);
        spinner_usertype = findViewById(R.id.spinner_usertype);
        ed_firstname = findViewById(R.id.ed_firstname);
        ed_lastname = findViewById(R.id.ed_lastname);
        ed_dob = findViewById(R.id.ed_dob);
        ed_contact = findViewById(R.id.ed_contact);
        ed_address = findViewById(R.id.ed_address);
        ed_bio = findViewById(R.id.ed_bio);
        ed_city = findViewById(R.id.ed_city);
        ed_country = findViewById(R.id.ed_country);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        txt_uploadimage = findViewById(R.id.txt_uploadimage);
        image = findViewById(R.id.image);
        btn_register = findViewById(R.id.btn_register);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_text, new String[]{"Choose Gender","Male", "Female", "Transgender"});
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_gender.setAdapter(adapter1);
        spinner_gender.setSelection(0);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_text, new String[]{"Language","English", "Hindi", "French","German","Russian"});
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_language.setAdapter(adapter2);
        spinner_language.setSelection(0);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.spinner_text, new String[]{"UserType","Admin", "Doctor", "Front-Office"});
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_usertype.setAdapter(adapter3);
        spinner_usertype.setSelection(0);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, R.layout.spinner_text, new String[]{"Specialization","Gynecologist", "Pediatrics", "Surgery", "Anesthesiology", "Orthopedics","Otorhinolaryngology","Psychiatry"});
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner_specialization.setAdapter(adapter4);
        spinner_specialization.setSelection(0);


        txt_uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity2.this);
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
                 selectedImage = result.getData().getData();
                image.setImageURI(selectedImage);
            }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if(result.getResultCode()==RESULT_OK){
              if(result.getData().getExtras().get("data")!=null){
                  capturedImage = (Bitmap) result.getData().getExtras().get("data");

                  image.setImageBitmap(capturedImage);
              }/*else {
                   selectedImage = result.getData().getData();
                  try {
                      capturedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }*/

            }
                });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = ed_firstname.getText().toString();
                String lastname = ed_lastname.getText().toString();
                String gender = spinner_gender.getSelectedItem().toString();
                String dob = ed_dob.getText().toString();
                String contact = ed_contact.getText().toString();
                String city  = ed_city.getText().toString();
                String country  = ed_country.getText().toString();
                String language = spinner_language.getSelectedItem().toString();
                String usertype = spinner_usertype.getSelectedItem().toString();
                String specialization = spinner_specialization.getSelectedItem().toString();
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                String address = ed_address.getText().toString();
                String bio = ed_bio.getText().toString();

                mAuth = FirebaseAuth.getInstance();


                if(selectedImage!=null || capturedImage!=null){
                    imagebitmap = capturedImage;
                    if(imagebitmap==null){
                        try {
                            imagebitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(RegisterActivity2.this, "Please select a profile image", Toast.LENGTH_SHORT).show();
                }
                String filename = UUID.randomUUID().toString() + ".jpg";

                StorageReference storageref = FirebaseStorage.getInstance().getReference().child("images/" + filename);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();
                UploadTask uploadTask = storageref.putBytes(imageData);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageurl = uri.toString();
                                    mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser firebaseUser = task.getResult().getUser();
                                                String Uid = firebaseUser.getUid();
                                                ClinicUser user = new ClinicUser(firstname, lastname, gender, dob, contact, city, country, language, usertype, specialization, address, bio, imageurl);
                                                FirebaseDatabase.getInstance().getReference().child("ClinicUser").child(Uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(RegisterActivity2.this, "User Created Successfully", Toast.LENGTH_SHORT).show();


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity2.this, "Failed to Create User", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }else {
                                                Toast.makeText(RegisterActivity2.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity2.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });


    }
}