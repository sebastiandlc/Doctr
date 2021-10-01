package com.group.poop.doctr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Thread.sleep;

public class Interstitial extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get a reference to the docotr list of the database
            DatabaseReference doctorRef = mDatabase.child("doctorsList").child(currentUser.getUid());

            // Read from the database
            doctorRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isDoctor = dataSnapshot.exists();
                    if (isDoctor) {
                        Intent intent = new Intent(Interstitial.this, NewDoctorHome.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });

            DatabaseReference userProfilesRef = mDatabase.child("UserProfiles").child(currentUser.getUid());
            userProfilesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isUser = dataSnapshot.exists();
                    if (isUser) {
                        Intent intent = new Intent(Interstitial.this, UserHome.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            DatabaseReference profileNotCompleteRef = mDatabase.child("ProfileNotComplete").child(currentUser.getUid());
            profileNotCompleteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isNotComplete = dataSnapshot.exists();
                    if (isNotComplete) {
                        Intent intent = new Intent(Interstitial.this, SignUpChoicePage.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else {
            Intent intent = new Intent(Interstitial.this, LoginPage.class);
            startActivity(intent);
        }
    }
}
