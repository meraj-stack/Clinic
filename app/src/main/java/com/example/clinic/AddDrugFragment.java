package com.example.clinic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddDrugFragment extends Fragment {
   private EditText edit_tradename, edit_genericname, edit_note;
     private  Button btn_adddrug, btn_search;
     private AlertDialog alertDialog;
     private List<Drugs> drugsList = new ArrayList<>();
     private DrugAdapter drugAdapter;
     private RecyclerView recycler_drug;
     private SearchView searchView;
     private RecyclerView recycler_suggestion;
     private List<String> suggestionList = new ArrayList<>();
     private CursorAdapter suggestionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_drug, container, false);
        edit_genericname = view.findViewById(R.id.edit_genericname);
        edit_tradename = view.findViewById(R.id.edit_tradename);
        edit_note = view.findViewById(R.id.edit_note);
        btn_adddrug = view.findViewById(R.id.btn_adddrug);
        btn_search = view.findViewById(R.id.btn_search);
        recycler_drug = view.findViewById(R.id.recycler_drugs);
        searchView = view.findViewById(R.id.search_drug);



        recycler_drug.setLayoutManager(new LinearLayoutManager(requireContext()));
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Drugs");
        drugAdapter = new DrugAdapter(requireContext(), dbref);
        recycler_drug.setAdapter(drugAdapter);




        View alertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alertdialog_drug, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertDialogView);
        alertDialog = builder.create();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btn_ok = alertDialogView.findViewById(R.id.btn_ok);


        suggestionAdapter = new CursorAdapter(getContext(), null , 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView) view;
                @SuppressLint("Range") String suggestion = cursor.getString(cursor.getColumnIndex("tradename"));
                textView.setText(suggestion);

            }
        };

        searchView.setSuggestionsAdapter(suggestionAdapter);










        btn_adddrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Adding Drug...");
                progressDialog.show();
                String tradename = edit_tradename.getText().toString();
                String genericname = edit_genericname.getText().toString();
                String note = edit_note.getText().toString();
                if (tradename!=null && genericname!=null){
                    Drugs drugs = new Drugs(tradename, genericname, note);
                    String drugKey = dbref.push().getKey();
                    dbref.child(drugKey).setValue(drugs).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            Toast.makeText(requireContext(), "Failed to Add Drug", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(requireContext(), "Trade Name and Generic Name should not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = suggestionAdapter.getCursor();
                if(cursor!=null && cursor.moveToPosition(position)){
                    @SuppressLint("Range") String selectedSuggestion = cursor.getString(cursor.getColumnIndex("tradename"));
                    searchView.setQuery(selectedSuggestion, true);
                   filterData(selectedSuggestion);
                }


                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSuggestions(newText);
                return true;
            }
        });
















        return view;
    }

    private void filterData(String searchText){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Drugs");
        Query query = dr.orderByChild("tradename").startAt(searchText).endAt(searchText + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugsList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()
                     ) {
                    Drugs drugs = snapshot.getValue(Drugs.class);
                    drugsList.add(drugs);

                }

                drugAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterSuggestions(String searchText){
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Drugs");
        Query query =dref.orderByChild("tradename").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MatrixCursor suggestionsCursor = new MatrixCursor(new String[] { BaseColumns._ID, "tradename" });
                int index = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String tradename = dataSnapshot.child("tradename").getValue(String.class);
                    suggestionsCursor.addRow(new Object[] { index, tradename });
                    index++;

                }
                suggestionAdapter.swapCursor(suggestionsCursor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}