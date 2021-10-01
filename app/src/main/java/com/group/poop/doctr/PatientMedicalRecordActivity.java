package com.group.poop.doctr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

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

public class PatientMedicalRecordActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private Button createpdf_Button;
    private User user = null;
    private String uid;
    private ToggleButton hideMR;

    PDFTester pdf = new PDFTester(this);

    private List<MedicalRecord> mr_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_medical_record);

        mRecycler = (RecyclerView)findViewById(R.id.p_medicalRecordsRecyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        //Array List for the Medical record object
        mr_list = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getUid();

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

        query = FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(uid).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);

                    //Functionality for Toggle:
                    hideMR = findViewById(R.id.toggleButtonMR);
                    hideMR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                // The toggle is enabled
                                user.setShowMR(false);
                                FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(uid).child("showMR").setValue(false);
                            } else {
                                // The toggle is disabled
                                user.setShowMR(true);
                                FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(uid).child("showMR").setValue(true);
                            }
                        }
                    });

                    //Functionality for Generating PDF
                    createpdf_Button = findViewById(R.id.createPDF);
                    createpdf_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pdf.createPdf(user,mr_list);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

}
