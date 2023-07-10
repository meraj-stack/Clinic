package com.example.clinic;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class DoctorSettingFragment extends Fragment {
    private EditText edit_mail, ed_Firstname, ed_Lastname, ed_Contact, ed_City, ed_Country, ed_Address, ed_Gender, ed_DOB, ed_Specialization, ed_Email;
    private Button btn_sendotp, btn_saveChanges;
    private CircularImageView doctorImage;
    private TextView txt_upplaodimage;

    private FirebaseAuth firebaseAuth;
    private String verificationRequestId;
    private String otp;
    private TextView txt_otp, txt_newpassword, txt_confirmpassword;
    private String generatedOTP;
    private DatabaseReference dbr;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Bitmap capturedImage = null;
    private Uri selectedImage = null;
    private Bitmap imagebitmap = null;
    private Bitmap uneditedImage = null;
    private Uri uneditedimageuri = null;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_setting, container, false);
        edit_mail = view.findViewById(R.id.edit_mail);
        ed_Email = view.findViewById(R.id.ed_Email);
        ed_Firstname = view.findViewById(R.id.ed_Firstname);
        ed_Lastname = view.findViewById(R.id.ed_Lastname);
        ed_Gender = view.findViewById(R.id.ed_Gender);
        ed_Specialization = view.findViewById(R.id.ed_Specialization);
        ed_DOB = view.findViewById(R.id.ed_DOB);
        ed_Address = view.findViewById(R.id.ed_Address);
        ed_City = view.findViewById(R.id.ed_City);
        ed_Contact = view.findViewById(R.id.ed_Contact);
        ed_Country = view.findViewById(R.id.ed_Country);

        btn_sendotp = view.findViewById(R.id.btn_sendotp);
        btn_saveChanges = view.findViewById(R.id.btn_saveChanges);

        doctorImage = view.findViewById(R.id.doctorImage);

        txt_otp = view.findViewById(R.id.txt_otp);
        txt_upplaodimage = view.findViewById(R.id.txt_uploadimage);



        firebaseAuth = FirebaseAuth.getInstance();
        String Uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        dbr = FirebaseDatabase.getInstance().getReference().child("ClinicUser").child(Uid);

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstname = snapshot.child("firstname").getValue(String.class);
                String lastname = snapshot.child("lastname").getValue(String.class);
                String contact = snapshot.child("contact").getValue(String.class);
                String specialization = snapshot.child("specialization").getValue(String.class);
                String city = snapshot.child("city").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String country = snapshot.child("country").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);
                String dob = snapshot.child("dob").getValue(String.class);
                String doctorimage = snapshot.child("imageurl").getValue(String.class);

                String email = firebaseAuth.getCurrentUser().getEmail();


                ed_Address.setText(address);
                ed_Email.setText(email);
                ed_Firstname.setText(firstname);
                ed_Lastname.setText(lastname);
                ed_DOB.setText(dob);
                ed_Gender.setText(gender);
                ed_City.setText(city);
                ed_Country.setText(country);
                ed_Contact.setText(contact);
                ed_Specialization.setText(specialization);
                Glide.with(requireContext()).load(doctorimage).into(doctorImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txt_upplaodimage.setOnClickListener(new View.OnClickListener() {
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
                        selectedImage = result.getData().getData();
                        doctorImage.setImageURI(selectedImage);
                    }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()==RESULT_OK){
                        if(result.getData().getExtras().get("data")!=null) {
                            capturedImage = (Bitmap) result.getData().getExtras().get("data");

                            doctorImage.setImageBitmap(capturedImage);
                        }

                    }
                });





        btn_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = ed_Address.getText().toString();
                String contact = ed_Contact.getText().toString();
                String city = ed_City.getText().toString();
                String country = ed_Country.getText().toString();


               dbr.child("contact").setValue(contact);
               dbr.child("city").setValue(city);
               dbr.child("country").setValue(country);
               dbr.child("address").setValue(address)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               if(selectedImage!=null || capturedImage!=null){
                                   imagebitmap = capturedImage;
                                   if(imagebitmap==null){
                                       try {
                                           imagebitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                                       }catch (IOException e){
                                           e.printStackTrace();
                                       }
                                   }
                               }else {
                                   BitmapDrawable imageDrawable = (BitmapDrawable) doctorImage.getDrawable();
                                   uneditedImage=imageDrawable.getBitmap();
                                   imagebitmap = uneditedImage;
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
                                                   dbr.child("imageurl").setValue(imageurl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           Toast.makeText(requireContext(), "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception e) {
                                                           Toast.makeText(requireContext(), "Error Saving Changes", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });

                                               }
                                           });
                                       }
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(requireContext(), "Failed To Change Profile Image", Toast.LENGTH_SHORT).show();
                                   }
                               });


                           }

                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(requireContext(), "Error Saving Data", Toast.LENGTH_SHORT).show();
                           }
                       });


            }
        });




        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_mail.getText().toString();
                sendOtp(email);

            }
        });





        return view;
    }

    private void sendOtp(String email) {
        generatedOTP = generateOTP(4);
        saveOTP(generatedOTP);
        sendEmailOTP(email, generatedOTP);
        txt_otp.setVisibility(View.VISIBLE);
        String markedemail = maskEmail(email);
        String verificationtext = "A Verification Link has been sent to " + markedemail + ", Please Verify the Link To Reset Your Password";
        txt_otp.setText(verificationtext);




    }


    private void resetPassword( String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Password reset successful
                        Toast.makeText(requireContext(), "Password reset successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // Password reset failed
                        Toast.makeText(requireContext(), "Password reset failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private String generateOTP(int length){
        String allowedChars = "0123456789";

        // Create a StringBuilder to store the generated OTP
        StringBuilder otpBuilder = new StringBuilder(length);

        // Generate random characters from the allowedChars string until the OTP length is reached
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            otpBuilder.append(allowedChars.charAt(index));
        }

        // Return the generated OTP as a string
        return otpBuilder.toString();
    }

    private void saveOTP(String otp){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OTP_KEY", otp);
        editor.apply();
    }

    private String getSavedOTP(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("OTP_KEY", "");
    }

    private void sendEmailOTP(String email, String otp){
        String subject = "OTP Verification";
        String message = "Your OTP is For Password Reset is : " + otp;

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(requireContext(), "Verification Link send to Your Email", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(requireContext(), "Failed to send Link", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private String maskEmail(String email) {
        StringBuilder maskedEmail = new StringBuilder();
        int atIndex = email.indexOf('@');
        if (atIndex >= 3) {
            maskedEmail.append(email.substring(0, 3));
            maskedEmail.append("****");
            maskedEmail.append(email.substring(atIndex));
        } else {
            maskedEmail.append(email);
        }
        return maskedEmail.toString();
    }



}