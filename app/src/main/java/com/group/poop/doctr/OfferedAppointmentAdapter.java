package com.group.poop.doctr;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class OfferedAppointmentAdapter extends RecyclerView.Adapter<OfferedAppointmentAdapter.OfferedAppointmentViewHolder> {

    List<Appointment> appointments;
    Context C;

    OfferedAppointmentAdapter(List<Appointment> appointments, Context C) {
        this.appointments = appointments;
        this.C = C;
    }

    @Override
    public OfferedAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offered_appointment_card, parent, false);
        OfferedAppointmentViewHolder oavh = new OfferedAppointmentViewHolder(view);


        return oavh;
    }

    @Override
    public void onBindViewHolder(OfferedAppointmentViewHolder holder, final int position) {

        holder.speciality.setText(appointments.get(position).speciality);

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

        if(appointments.get(position).getPatientUID() == "NULL" || appointments.get(position).getPatientUID().equals("NULL")){
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
        }

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // open patient MR
                ref.child("DoctorsPatients").child(appointments.get(position).getDoctorUID()).child(appointments.get(position).getPatientUID()).setValue(true);
                final String PID = appointments.get(position).getDoctorUID();
                // open patient MR
                ref.child("UserProfiles").child(appointments.get(position).getPatientUID()).child("showMR").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean showMR = dataSnapshot.getValue(boolean.class);
                        Intent intent = new Intent(C, DoctorMedicalRecordActivity.class);
                        intent.putExtra("UID", PID);
                        intent.putExtra("SHOWMR", showMR);
                        C.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // notify patient

                // set appt flag to accepted
                appointments.get(position).setAccepted(1);
                Appointment appt = appointments.get(position);
                ref.child("AppointmentProfiles").child(appt.getDoctorUID()).child(appt.getApptKey()).child("accepted").setValue(1);
                ref.child("MedicalRecords").child(appt.getPatientUID()).push()
                        .setValue(new MedicalRecord(appt.getSpeciality(), appt.getStartTime(),
                                appt.getDescription(), appt.getDoctorUID(), appt.getDoctorName()));
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // notify patient

                // set appt flag to rejected
                appointments.get(position).setAccepted(-1);

                ref.child("AppointmentProfiles").child(appointments.get(position).getDoctorUID()).child(appointments.get(position).getApptKey()).child("accepted").setValue(-1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class OfferedAppointmentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView speciality;
        TextView price;
        TextView location;
        TextView date;
        TextView startTime;
        TextView endTime;
        Button accept;
        Button reject;

        OfferedAppointmentViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.offered_appointment_card_view);
            speciality = itemView.findViewById(R.id.offered_appointment_speciality);
            price = itemView.findViewById(R.id.offered_appointment_price);
            location = itemView.findViewById(R.id.offered_appointment_location);
            date = itemView.findViewById(R.id.offered_appointment_date);
            startTime = itemView.findViewById(R.id.offered_appointment_start);
            endTime = itemView.findViewById(R.id.offered_appointment_end);
            accept = itemView.findViewById(R.id.offered_appointment_accept);
            reject = itemView.findViewById(R.id.offered_appointment_reject);

        }
    }


}
