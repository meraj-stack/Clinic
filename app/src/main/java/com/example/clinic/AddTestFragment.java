package com.example.clinic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTestFragment extends Fragment {
    private EditText edit_testname, edit_testdescription;
    private Button btn_addtest;
    private RecyclerView recycler_test;
    private AlertDialog alertDialog;
    private List<Test> testList;
    private TestAdapter testAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_test, container, false);
        edit_testname = view.findViewById(R.id.edit_testname);
        edit_testdescription= view.findViewById(R.id.edit_testdescription);
        btn_addtest = view.findViewById(R.id.btn_addtest);
        recycler_test = view.findViewById(R.id.recycler_test);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Tests");

        View alertDialogView = LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_test, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertDialogView);
        alertDialog = builder.create();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btn_ok = alertDialogView.findViewById(R.id.btn_ok);

        testList = new ArrayList<>();
        recycler_test.setLayoutManager(new LinearLayoutManager(requireContext()));
        testAdapter = new TestAdapter(requireContext(), testList);
        recycler_test.setAdapter(testAdapter);


        btn_addtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Adding Test...");
                progressDialog.show();
                String testname = edit_testname.getText().toString();
                String testdescription = edit_testdescription.getText().toString();

                if(testname!=null && testdescription!=null){
                    Test test = new Test(testname, testdescription);
                    String testKey = dref.push().getKey();
                    dref.child(testKey).setValue(test).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            alertDialog.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to add Test", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Test Name and Description Should not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                testList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Test test =  dataSnapshot.getValue(Test.class);
                    if (test!=null){
                        test.setId(dataSnapshot.getKey());
                        testList.add(test);
                    }

                }

                testAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Unable to Fetch Data", Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }
}