package com.group.poop.doctr;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoctorMedicalRecordActivity extends AppCompatActivity {

    // Recycler
    private RecyclerView mRecycler;

    // Buttons
    private Button createpdf_Button;
    private Button saveapptdesc_Button;
    private TextInputEditText apptDescription;

    // PDF Tester
    PDFTester pdf = new PDFTester(this);

    // User Info
    private static User patientUser;
    private static String uid;
    private static String appKey;
    private List<MedicalRecord> mr_list;

    // Constants
    public static final String UID_PARAM = "UID";
    public static final String SHOW_MR_PARAM = "SHOWMR";
    public static final String APPOINTMENT_KEY = "APPOINTMENT_KEY";

    // FireBase
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        mRecycler = (RecyclerView)findViewById(R.id.medicalRecordsRecyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        //Array List for the Medical record object
        mr_list = new ArrayList<MedicalRecord>();

        // Initialize Views
        createpdf_Button = findViewById(R.id.createPDF);
        saveapptdesc_Button = findViewById(R.id.saveApptDesc);
        apptDescription = findViewById(R.id.apptDescription);

        // Get UID
        // TODO: This was giving me a error so I had to comment it out.
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString(UID_PARAM);
        appKey = bundle.getString(APPOINTMENT_KEY);
        boolean showMr = true;//bundle.getBoolean(SHOW_MR_PARAM);

        // Retrieve the patientUser
        requestPatientUser();

        // Save Appointment Button
        saveapptdesc_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code for saving appointment description goes here.

                // Retrieve Appointment Description
                String appDescText = (String) apptDescription.getText().toString().trim();

                boolean invalidString = (appDescText == null) || (appDescText.length() == 0);

                if( invalidString )
                {
                    Toast.makeText(DoctorMedicalRecordActivity.this, "Nothing to save!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if( (uid != null )) {
                        // Clear it
                        apptDescription.setText(null);

                        // Save it
                        saveMedicalNote(appDescText);

                        // Provide user visual-feedback.
                        Toast.makeText(DoctorMedicalRecordActivity.this, "Medical note saved!", Toast.LENGTH_SHORT).show();
                    }else if( uid == null){
                            Toast.makeText(DoctorMedicalRecordActivity.this, "(uid == null)", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });

        if(showMr){

            // Get All Medical Records
            //Query query = FirebaseDatabase.getInstance().getReference().child("MedicalRecords").child(uid).orderByKey();

            //

            Query query = mDatabase.child("MedicalRecords").child(uid).orderByKey();
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.exists()){
                        mr_list.add(dataSnapshot.getValue(MedicalRecord.class));
                        mRecycler.setAdapter(new MedicalRecordAdapter(mr_list));
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        Query query = FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(uid).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    patientUser = dataSnapshot.getValue(User.class);
                    //Functionality for Generating PDF
                    createpdf_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pdf.createPdf(patientUser,mr_list);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private static void requestPatientUser()
    {
//        mDatabase.child("UserProfiles").child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                String patientString = (String) snapshot.getValue().toString();
//                patientUser = new User(patientString);
//                // TODO - Place your code here
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//
//        });

    }

    private void saveMedicalNote(String medicalNote)
    {
        // TODO - Save medical record to the patients data base.
        //mDatabase.child("MedicalRecords").child(uid).child("")setValue(patientUser);

    }
}
