package com.example.clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {
    private Context context;
    private List<CreateInvoice> list;
    public ReportsAdapter(Context context, List<CreateInvoice> list){
        this.context= context;
        this.list= list;


    }
    @NonNull
    @Override
    public ReportsAdapter.ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportlayout, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ReportsViewHolder holder, int position) {
        CreateInvoice createInvoice = list.get(position);
        holder.text_doctorName.setText(createInvoice.getDoctorname());
        holder.text_patientName.setText(createInvoice.getPatientname());
        holder.text_appDate.setText(createInvoice.getDate());
        holder.text_billingAmount.setText(createInvoice.getDoctorfee());
        holder.text_billingDate.setText(createInvoice.getDate());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder{
        private TextView text_doctorName, text_patientName, text_appDate, text_billingAmount, text_billingDate;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            text_doctorName = itemView.findViewById(R.id.text_doctorName);
            text_patientName = itemView.findViewById(R.id.text_patientName);
            text_appDate = itemView.findViewById(R.id.text_appDate);
            text_billingAmount = itemView.findViewById(R.id.text_billingAmount);
            text_billingDate = itemView.findViewById(R.id.text_billingDate);
        }
    }
}
