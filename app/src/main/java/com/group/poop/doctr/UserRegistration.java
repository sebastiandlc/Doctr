package com.group.poop.doctr;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserRegistration extends AppCompatActivity {
    private DatabaseReference database;

    // Name
    private TextInputLayout firstNameLayout;
    private EditText firstName;
    private TextInputLayout lastNameLayout;
    private EditText lastName;

    // Date Of Birth
    private TextInputLayout dobLayout;
    private EditText dob;
    private Calendar birthday;

    // Gender
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;

    // Body Statistics
    private TextInputLayout heightLayout;
    private EditText height;
    private TextInputLayout weightLayout;
    private EditText weight;

    // Medical Info
    private TextInputLayout allergiesLayout;
    private EditText allergies;
    private TextInputLayout medicationsLayout;
    private EditText medications;

    // Registration
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
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
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize Calendar
        birthday = Calendar.getInstance();

        // Initialize Layouts and EditTexts and Buttons
        firstNameLayout = findViewById(R.id.firstNameLayout);
        firstName = firstNameLayout.getEditText();
        lastNameLayout = findViewById(R.id.lastNameLayout);
        lastName = lastNameLayout.getEditText();
        dobLayout = findViewById(R.id.dobLayout);
        dob = dobLayout.getEditText();
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        heightLayout = findViewById(R.id.heightLayout);
        height = heightLayout.getEditText();
        weightLayout = findViewById(R.id.weightLayout);
        weight = weightLayout.getEditText();

        allergiesLayout = findViewById(R.id.allergiesLayout);
        allergies = allergiesLayout.getEditText();
        medicationsLayout = findViewById(R.id.medicationsLayout);
        medications = medicationsLayout.getEditText();

        mRegister = findViewById(R.id.register);

        //Filters for input
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

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0){
                    long numPounds = Long.parseLong(text);
                    if (numPounds <= 0 || numPounds > 110) {
                        heightLayout.setError("Error: Height must be between 1 - 110 inches.");
                    } else {
                        heightLayout.setError(null);
                    }
                }
            }
        });

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0) {
                    long numPounds = Long.parseLong(text);
                    if (numPounds <= 0 || numPounds > 2000) {
                        weightLayout.setError("Error: Weight must be between 1 - 2,000 lbs.");
                    } else {
                        weightLayout.setError(null);
                    }
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserRegistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday.set(year, month, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        String text = format.format(birthday.getTime());
                        dob.setText(text);

                        Calendar minimum = Calendar.getInstance();
                        minimum.add(Calendar.YEAR, -18);
                        if (birthday.getTime().compareTo(minimum.getTime()) > 0) {
                            dobLayout.setError("Error: You must be 18 or older.");
                        } else {
                            dobLayout.setError(null);
                        }
                    }
                },1995,0,0).show();

            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorFree = (firstNameLayout.getError() == null)
                        && (lastNameLayout.getError() == null)
                        && (dobLayout.getError() == null)
                        && (heightLayout.getError() == null)
                        && (weightLayout.getError() == null);

                boolean filledOut = (firstName.getText().length() > 0)
                        && (lastName.getText().length() > 0)
                        && (dob.getText().length() > 0)
                        && (gender.getCheckedRadioButtonId() > -1)
                        && (height.getText().length() > 0)
                        && (weight.getText().length() > 0);

                if (!errorFree) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistration.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistration.this);
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
                    String genderText = "ERROR";
                    if (male.isChecked()) {
                        genderText = "male";
                    }
                    else if (female.isChecked()) {
                        genderText = "female";
                    }

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    User user = new User(uid,
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            birthday.getTime(),
                            genderText,
                            Long.parseLong(height.getText().toString()),
                            Long.parseLong(weight.getText().toString()),
                            allergies.getText().toString(),
                            medications.getText().toString());

                    database.child("UserProfiles").child(uid).setValue(user);
                    database.child("ProfileNotComplete").child(uid).removeValue();

                    Intent intent = new Intent(UserRegistration.this, UserHome.class);
                    startActivity(intent);
                }
            }
        });
    }
}
