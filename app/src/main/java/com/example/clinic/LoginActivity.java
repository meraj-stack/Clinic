package com.example.clinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    TextView txt_register;
    private EditText edit_username, edit_password;
    private Button btn_login;
    LinearLayout linearlayout;
    private TextView txt_facebook;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_register = findViewById(R.id.txt_register);
        linearlayout = findViewById(R.id.linearlayout);

        edit_password = findViewById(R.id.edit_password);
        edit_username = findViewById(R.id.edit_username);
        btn_login = findViewById(R.id.btn_login);
        txt_facebook = findViewById(R.id.txt_facebook);









        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity2.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String username = edit_username.getText().toString();
                String password = edit_password.getText().toString();
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Signing in...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(false);

                View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_progressbar, null);
                progressDialog.setView(view);


                ProgressBar progressBar = view.findViewById(R.id.progressBar);

                progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C0FF")));




                progressDialog.show();

                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("ClinicUser").child(uid);
                            userref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String usertype = snapshot.child("usertype").getValue(String.class);
                                    switch (usertype) {
                                        case "Doctor":
                                            String firstname = snapshot.child("firstname").getValue(String.class);
                                            String lastname = snapshot.child("lastname").getValue(String.class);
                                            String specialization = snapshot.child("specialization").getValue(String.class);
                                            String imageuri = snapshot.child("imageurl").getValue(String.class);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                                            intent.putExtra("first", firstname);
                                            intent.putExtra("last", lastname);
                                            intent.putExtra("special", specialization);
                                            intent.putExtra("imageuri", imageuri);
                                            startActivity(intent);
                                            break;
                                        case "Admin":
                                            String first = snapshot.child("firstname").getValue(String.class);
                                            String last = snapshot.child("lastname").getValue(String.class);
                                            String image = snapshot.child("imageurl").getValue(String.class);
                                            Intent intent1 = new Intent(LoginActivity.this, MainActivity2.class);
                                            intent1.putExtra("firstname", first);
                                            intent1.putExtra("lastname", last);
                                            intent1.putExtra("image", image);
                                            startActivity(intent1);
                                            break;
                                        case "Front-Office":
                                            String firstName = snapshot.child("firstname").getValue(String.class);
                                            String lastName = snapshot.child("lastname").getValue(String.class);
                                            String Image = snapshot.child("imageurl").getValue(String.class);
                                            Intent intent2 = new Intent(LoginActivity.this,MainActivity3.class);
                                            intent2.putExtra("First", firstName);
                                            intent2.putExtra("Last", lastName);
                                            intent2.putExtra("Image", Image);
                                            startActivity(intent2);
                                            break;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }else{
                            Toast.makeText(LoginActivity.this, "failed to sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}