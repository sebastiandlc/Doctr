package com.group.poop.doctr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorRegistration extends AppCompatActivity {

    private ArrayList<String> specializationsList;
    private DatabaseReference ref;

    private Button finish;
    private Spinner specialization;
    private Spinner subSpecialization;
    private EditText firstName;
    private TextInputLayout firstNameLayout;
    private EditText lastName;
    private TextInputLayout lastNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        ref = FirebaseDatabase.getInstance().getReference();

        specializationsList = new ArrayList<String>();

        specialization = findViewById(R.id.specSelect);
        subSpecialization = findViewById(R.id.subSpecSelect);
        finish = findViewById(R.id.register);
        firstNameLayout = findViewById(R.id.firstNameLayout);
        firstName = firstNameLayout.getEditText();
        lastNameLayout = findViewById(R.id.lastNameLayout);
        lastName = lastNameLayout.getEditText();

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Retrieve and Set Final Button Lable
        final String EXTRA_MESSAGE = "finishButtonLable";
        Bundle extras = getIntent().getExtras();
        String newString;
        if(extras == null) {
            newString= null;
        } else {
            newString= extras.getString(EXTRA_MESSAGE);
            finish.setText(newString);
        }

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                // Check to make sure name consists of only alphabetic characters
                if (!text.matches("\\p{L}+") && text.length() > 0) {
                    firstNameLayout.setError("Error: First Name may only contain letters.");
                } else {
                    firstNameLayout.setError(null);
                }
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                // Check to make sure name consists of only alphabetic characters
                if (!text.matches("\\p{L}+") && text.length() > 0) {
                    firstNameLayout.setError("Error: Last Name may only contain letters.");
                } else {
                    firstNameLayout.setError(null);
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean errorFree = (firstNameLayout.getError() == null)
                        && (lastNameLayout.getError() == null);

                boolean filledOut = (firstName.getText().length() > 0)
                        && (lastName.getText().length() > 0)
                        && (specialization.getSelectedItem().toString() != null)
                        && (subSpecialization.getSelectedItem().toString() != null);

                if (!errorFree) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoctorRegistration.this);
                    builder.setTitle("Registration Error");
                    builder.setMessage("Please fix the indicated errors in your registration form to continue");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else if (!filledOut) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoctorRegistration.this);
                    builder.setTitle("Registration Error");
                    builder.setMessage("Please complete all fields of the registration form to continue");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Doctor doc = new Doctor(uid,
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            specialization.getSelectedItem().toString(),
                            subSpecialization.getSelectedItem().toString());

                    ref.child("DoctorProfiles").child(uid).setValue(doc);
                    ref.child("ProfileNotComplete").child(uid).removeValue();
                    ref.child("doctorsList").child(uid).setValue(true);

                    Intent intent = new Intent(DoctorRegistration.this, NewDoctorHome.class);
                    startActivity(intent);
                }
            }
        });

        Query query = ref.child("Specialization").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot specialization : dataSnapshot.getChildren()) {
                        specializationsList.add(specialization.getKey());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoctorRegistration.this, android.R.layout.simple_spinner_dropdown_item, specializationsList);

                    specialization.setAdapter(adapter);
                    subSpecialization.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO add errors
            }
        });

    }
}
