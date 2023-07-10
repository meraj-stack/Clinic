package com.example.clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.DoctorAppointmentViewHolder> {
    private Context context;
    private List<Appointment> appointmentList;

    public DoctorAppointmentAdapter(Context context, List<Appointment> appointmentList){
        this.context=context;
        this.appointmentList=appointmentList;
    }
    @NonNull
    @Override
    public DoctorAppointmentAdapter.DoctorAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorappointmentlist, parent, false);
        return new DoctorAppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.DoctorAppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.txt_patientName.setText(appointment.getPateintname());
        holder.txt_Date.setText(appointment.getDate());
        holder.txt_Time.setText(appointment.getTime());
        holder.txt_status.setText(appointment.getStatus());
        String status = appointment.getStatus();
        int bgcolor;




        String timerange = appointment.getTime();
        String date = appointment.getDate();


            String[] timeParts = timerange.trim().split("-");
            String upperTimeLimit = timeParts[1].trim().split("\\s+")[0];


                // Compare the current time with the upper time limit and update the CardView

                // Handle the case when the time range format is incorrect


            if (isCurrentTimeLessThanOrEqualTo(upperTimeLimit)) {
                holder.txt_status.setText("Oncoming..");
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.frontcolor2));

            }else {
                holder.txt_status.setText("Completed..");
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.progresscolor));
            }


    }

    private boolean isCurrentTimeLessThanOrEqualTo(String upperTimeLimit) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Calendar currentCalendar = Calendar.getInstance();
            String currentFormattedTime = timeFormat.format(currentCalendar.getTime());


            // Extract hours and minutes from the time strings
            int currentHours = Integer.parseInt(currentFormattedTime.substring(0, 2));
            int currentMinutes = Integer.parseInt(currentFormattedTime.substring(3));
            int upperHours = Integer.parseInt(upperTimeLimit.substring(0, 2));
            int upperMinutes = Integer.parseInt(upperTimeLimit.substring(3));

            // Compare the extracted hours and minutes
            if (currentHours < upperHours) {

                return true;
            } else if (currentHours == upperHours && currentMinutes <= upperMinutes) {

                return true;
            } else {
             
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class DoctorAppointmentViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_patientName, txt_Date, txt_Time, txt_status;
        private CardView cardView;

        public DoctorAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_patientName = itemView.findViewById(R.id.text_patientname);
            txt_Date = itemView.findViewById(R.id.text_date);
            txt_Time = itemView.findViewById(R.id.text_time);
            txt_status = itemView.findViewById(R.id.txt_status);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
