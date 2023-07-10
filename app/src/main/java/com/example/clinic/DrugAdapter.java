package com.example.clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.DrugViewHolder> {
    private DatabaseReference dbref;
    private Context context;
    private List<Drugs> drugsList = new ArrayList<>();



    public DrugAdapter (Context context, DatabaseReference dbref){
        this.context=context;
        this.dbref=dbref;

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugsList.clear();
                for (DataSnapshot datasnapshot: snapshot.getChildren()
                     ) {
                    Drugs drugs = datasnapshot.getValue(Drugs.class);

                    if (drugs!=null){
                        drugs.setId(datasnapshot.getKey());
                        drugsList.add(drugs);
                    }

                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setData(List<Drugs> drugsList){
        this.drugsList=drugsList;
    }
    @NonNull
    @Override
    public DrugAdapter.DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_drug, parent, false);
        return new DrugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugAdapter.DrugViewHolder holder, int position) {
        Drugs drugs = drugsList.get(position);
        holder.txt_tradename.setText(drugs.getTradename());
        holder.txt_genericname.setText(drugs.getGenericname());
        holder.txt_note.setText(drugs.getNote());



    }

    @Override
    public int getItemCount() {
        return drugsList.size();
    }

    public  class DrugViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_tradename, txt_genericname, txt_note;
        private ImageView img_menu;


        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tradename= itemView.findViewById(R.id.txt_tradename);
            txt_genericname= itemView.findViewById(R.id.txt_genericname);
            txt_note= itemView.findViewById(R.id.txt_note);
            img_menu = itemView.findViewById(R.id.img_menu);
        }
    }
}
