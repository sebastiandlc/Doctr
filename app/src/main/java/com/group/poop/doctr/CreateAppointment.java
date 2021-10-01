package com.group.poop.doctr;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateAppointment extends AppCompatActivity {
    private DatabaseReference ref;

    //Name of Location
    private TextInputLayout locationLayout;
    private EditText locationName;

    //Name of Service
    private TextInputLayout serviceLayout;
    private EditText serviceName;

    //Description of Service
    private TextInputLayout descLayout;
    private EditText desc;

    //Date of Service
    private TextInputLayout dateLayout;
    private EditText date;

    //Start Time of Service
    private TextInputLayout sTimeLayout;
    private EditText sTime;

    //End Time of Service
    private TextInputLayout eTimeLayout;
    private EditText eTime;

    //Cost of Service
    private TextInputLayout costLayout;
    private EditText cost;

    private Button mCreate;

    private Calendar apptDate;
    private Calendar startTimeAppt;
    private Calendar endTimeAppt;

    static int hour, min;

    String doctorName;
    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Create Appointments");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        //Accessing name of Doctor from DB.
        ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference drNameRef = ref.child("DoctorProfiles").child(uid);
        drNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Doctor dr = dataSnapshot.getValue(Doctor.class);
                doctorName = ("Dr. " + dr.getFirstName() + " " + dr.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initialize Database
        ref = FirebaseDatabase.getInstance().getReference();

        // Initialize Calendar objects
        apptDate = Calendar.getInstance();
        startTimeAppt = Calendar.getInstance();
        endTimeAppt = Calendar.getInstance();
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Initialize Layouts and EditTexts and Buttons
        locationLayout = findViewById(R.id.locationLayout);
        locationName = locationLayout.getEditText();

        serviceLayout = findViewById(R.id.serviceLayout);
        serviceName = serviceLayout.getEditText();

        descLayout = findViewById(R.id.descLayout);
        desc = descLayout.getEditText();

        dateLayout = findViewById(R.id.dateLayout);
        date = dateLayout.getEditText();

        sTimeLayout = findViewById(R.id.sTimeLayout);
        sTime = sTimeLayout.getEditText();

        eTimeLayout = findViewById(R.id.eTimeLayout);
        eTime = eTimeLayout.getEditText();

        costLayout = findViewById(R.id.costLayout);
        cost = costLayout.getEditText();

        mCreate = findViewById(R.id.create);


        //Appointment Date Picker functionality.
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        apptDate.set(year, month, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        String text = format.format(apptDate.getTime());
                        date.setText(text);
                    }
                },2017,12,1).show();

            }
        });

        //Start Time Picker functionality
        sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td = new TimePickerDialog(CreateAppointment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTimeAppt.set(hourOfDay,minute);
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    sTime.setText(amPm );
                                } catch (Exception ex) {sTime.setText(ex.getMessage().toString());}
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(CreateAppointment.this)
                );
                td.show();
            }
        });

        //End Time Picker functionality
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td = new TimePickerDialog(CreateAppointment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTimeAppt.set(hourOfDay,minute);
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    eTime.setText(amPm );
                                } catch (Exception ex) {eTime.setText(ex.getMessage().toString());}
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(CreateAppointment.this)
                );
                td.show();
            }
        });

        //Creating a new appointment and filling it in
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorFree = (locationLayout.getError() == null)
                        && (serviceLayout.getError() == null)
                        && (descLayout.getError() == null)
                        && (dateLayout.getError() == null)
                        && (sTimeLayout.getError() == null)
                        && (eTimeLayout.getError() == null)
                        && (costLayout.getError() == null);

                boolean filledOut = (locationName.getText().length() > 0)
                        && (serviceName.getText().length() > 0)
                        && (desc.getText().length() > 0)
                        && (date.getText().length() > 0)
                        && (sTime.getText().length() > 0)
                        && (eTime.getText().length() > 0)
                        && (cost.getText().length() > 0);

                if (!errorFree) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAppointment.this);
                    builder.setTitle("Creation Error");
                    builder.setMessage("Please fix the indicated errors in your appointment form to continue.");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else if (!filledOut) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAppointment.this);
                    builder.setTitle("Creation Error");
                    builder.setMessage("Please complete all fields of the appointment form to continue.");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else {

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Appointment appointment = new Appointment(
                            locationName.getText().toString(),
                            apptDate.getTime(),
                            startTimeAppt.getTime(),
                            endTimeAppt.getTime(),
                            Long.parseLong(cost.getText().toString()),
                            serviceName.getText().toString(),
                            uid,
                            doctorName,
                            desc.getText().toString());

                    String apptKey = ref.child("AppointmentProfiles").child(uid).push().getKey();
                    Log.e("apptKey: " ,apptKey);
                    appointment.setApptKey(apptKey);
                    ref.child("AppointmentProfiles").child(uid).child(apptKey).setValue(appointment);


                    Toast.makeText(CreateAppointment.this, "Appointment Created.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CreateAppointment.this, NewDoctorHome.class);
                    startActivity(intent);
                }
            }
        });
    }
}
