package com.group.poop.doctr;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Search {
    String specialityFilter;
    Long minPriceFilter;
    Long maxPriceFilter;
    String locationFilter;
    Date earliestDateFilter;
    Date latestDateFilter;
    Date earliestTimeFilter;
    Date latestTimeFilter;
    List<Appointment> list;

    public Search(String specialityFilter,
                         Long minPriceFilter,
                         Long maxPriceFilter,
                         String locationFilter,
                         Date earliestDateFilter,
                         Date latestDateFilter,
                         Date earliestTimeFilter,
                         Date latestTimeFilter) {
        this.specialityFilter = specialityFilter;
        this.minPriceFilter = minPriceFilter;
        this.maxPriceFilter = maxPriceFilter;
        this.locationFilter = locationFilter;
        this.earliestDateFilter = earliestDateFilter;
        this.latestDateFilter = latestDateFilter;
        this.earliestTimeFilter = earliestTimeFilter;
        this.latestTimeFilter = latestTimeFilter;
    }

    public List<Appointment> search(){
        getAllAppointments();
        return filter(list);
    }

    private void getAllAppointments() {
         list = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AppointmentProfiles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    list.add(item.getValue(Appointment.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private List<Appointment> filter(List<Appointment> appointments) {
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {

            if (specialityFilter != null) {
                if (!specialityFilter.equals(appointment.speciality)){
                    continue;
                }
            }

            if (minPriceFilter != null) {
                if (minPriceFilter.compareTo(appointment.getPrice()) > 0){
                    continue;
                }
            }

            if (maxPriceFilter != null) {
                if (maxPriceFilter.compareTo(appointment.getPrice()) < 0) {
                    continue;
                }
            }

            if (locationFilter != null) {
                if (!locationFilter.equals(appointment.getLocation())){
                    continue;
                }
            }

            if (earliestDateFilter != null) {
                if (earliestDateFilter.compareTo(appointment.getDate()) > 0) {
                    continue;
                }
            }

            if (latestDateFilter != null) {
                if (latestDateFilter.compareTo(appointment.getDate()) < 0) {
                    continue;
                }
            }

            if (earliestTimeFilter != null) {
                if (earliestTimeFilter.compareTo(appointment.getStartTime()) > 0) {
                    continue;
                }
            }

            if (latestTimeFilter != null) {
                if (latestTimeFilter.compareTo(appointment.getEndTime()) < 0) {
                    continue;
                }
            }

            matches.add(appointment);
        }
        return matches;
    }
}
