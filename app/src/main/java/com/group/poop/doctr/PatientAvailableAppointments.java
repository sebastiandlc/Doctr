package com.group.poop.doctr;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PatientAvailableAppointments extends AppCompatActivity {

    private List<Appointment> appointments;
    private RecyclerView mRecycler;
    private String userName;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Available Appointments");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_available_appointments);

        mRecycler = findViewById(R.id.available_appointment_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        appointments = new ArrayList<Appointment>();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        uid = FirebaseAuth.getInstance().getUid();

        Query q = mDatabase.child("UserProfiles").child(uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    userName = user.getFirstName();

                    mDatabase.child("AppointmentProfiles").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String s) {
                            Appointment appointment;
                            if(snapshot.exists()) {
                                Log.e("Count " ,""+snapshot.getChildrenCount());
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    Log.e("value " ,""+postSnapshot.getValue());
                                    appointment = postSnapshot.getValue(Appointment.class);
                                    Log.e("value " ,""+appointment.getDoctorName());
                                    if(appointment.getPatientUID().equals("NULL"))
                                        appointments.add(appointment);
                                }
                                PatientAvailableAppointmentsAdapter oaa = new PatientAvailableAppointmentsAdapter(appointments,
                                        userName, PatientAvailableAppointments.this);
                                mRecycler.setAdapter(oaa);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot snapshot, String s) {
                            Appointment appointment;
                            if(snapshot.exists()) {
                                Log.e("Count " ,""+snapshot.getChildrenCount());
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    Log.e("value " ,""+postSnapshot.getValue());
                                    appointment = postSnapshot.getValue(Appointment.class);
                                    Log.e("value " ,""+appointment.getDoctorName());
                                    if(appointment.getPatientUID().equals("NULL"))
                                        appointments.add(appointment);
                                }
                                PatientAvailableAppointmentsAdapter oaa = new PatientAvailableAppointmentsAdapter(appointments,
                                        userName, PatientAvailableAppointments.this);
                                mRecycler.setAdapter(oaa);
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
