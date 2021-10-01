package com.group.poop.doctr;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseAPI {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static Doctor doctor;

    public static void writeCurrentUser(User user)
    {
        // NOTE - This is not working yet (I don't think).
        String userId = FirebaseAuth.getInstance().getUid();

        // TODO - Resolve whether it is a dr or profile
        mDatabase.child("UserProfiles").child(userId).setValue(user);
        mDatabase.child("DoctorProfiles").child(userId).setValue(user);

    }

    private static void requestCurrentDoctor()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        mDatabase.child("DoctorProfiles").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String doctorString = (String) snapshot.getValue().toString();
                doctor = new Doctor(doctorString);
                // TODO - Place your code here

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

}
