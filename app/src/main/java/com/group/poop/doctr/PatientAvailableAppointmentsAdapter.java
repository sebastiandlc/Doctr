package com.group.poop.doctr;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;



public class PatientAvailableAppointmentsAdapter extends RecyclerView.Adapter<PatientAvailableAppointmentsAdapter.PatientAvailableAppointmentsViewHolder> {

    List<Appointment> appointments;
    String userName;
    Context context;

    PatientAvailableAppointmentsAdapter(List<Appointment> appointments, String userName, Context context) {
        this.appointments = appointments;
        this.userName = userName;
        this.context = context;
    }

    @Override
    public PatientAvailableAppointmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_appointment_card, parent, false);
        PatientAvailableAppointmentsViewHolder oavh = new PatientAvailableAppointmentsViewHolder(view);
        return oavh;
    }

    @Override
    public void onBindViewHolder(PatientAvailableAppointmentsViewHolder holder, final int position) {

        holder.speciality.setText(appointments.get(position).speciality);

        holder.doctorName.setText(appointments.get(position).doctorName);


        holder.price.setText("$" + appointments.get(position).price.toString());

        holder.location.setText(appointments.get(position).location);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dateFormat.format(appointments.get(position).getDate().getTime());
        holder.date.setText(dateString);

        SimpleDateFormat startFormat = new SimpleDateFormat("hh:mm aaa");
        String startString = startFormat.format(appointments.get(position).getStartTime().getTime());
        holder.startTime.setText(startString);

        SimpleDateFormat endFormat = new SimpleDateFormat("hh:mm aaa");
        String endString = endFormat.format(appointments.get(position).getEndTime().getTime());
        holder.endTime.setText(endString);

        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getUid();
                Appointment appt = appointments.get(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                appt.setPatientUID(uid);

                ref.child("AppointmentProfiles").child(appt.getDoctorUID()).child(appt.getApptKey()).child("patientUID").setValue(uid);

                String conv_key = ref.child("Conversations").push().getKey();
                ref.child("Conversations").child(conv_key).setValue(new Conversation(appt.doctorName, uid, appt.doctorName, userName));

                ref.child("Messages").child(conv_key).push().setValue(new ChatMessage("Start talking!", "System", "NULL"));

                Toast.makeText(context, "Appointment Requested.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, UserHome.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class PatientAvailableAppointmentsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView doctorPhoto;
        TextView speciality;
        TextView doctorName;
        TextView price;
        TextView location;
        TextView date;
        TextView startTime;
        TextView endTime;
        Button request;

        PatientAvailableAppointmentsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.available_appointment_card_view);
            doctorPhoto = itemView.findViewById(R.id.available_appointment_dr_photo);
            speciality = itemView.findViewById(R.id.available_appointment_speciality);
            doctorName = itemView.findViewById(R.id.available_appointment_dr_name);
            price = itemView.findViewById(R.id.available_appointment_price);
            location = itemView.findViewById(R.id.available_appointment_location);
            date = itemView.findViewById(R.id.available_appointment_date);
            startTime = itemView.findViewById(R.id.available_appointment_start);
            endTime = itemView.findViewById(R.id.available_appointment_end);
            request = itemView.findViewById(R.id.available_appointment_request);
        }
    }


}
