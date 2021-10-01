package com.group.poop.doctr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private EditText email;
    private TextInputLayout passwordLayout;
    private EditText password;
    private TextInputLayout confirmPasswordLayout;
    private EditText confirmPassword;
    private Button mSignUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // Initialize fields
        emailLayout = findViewById(R.id.emailLayout);
        email = emailLayout.getEditText();
        passwordLayout = findViewById(R.id.passwordLayout);
        password = passwordLayout.getEditText();
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        confirmPassword = confirmPasswordLayout.getEditText();
        mSignUp = findViewById(R.id.signUp);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = email.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                    emailLayout.setError("Error: Please enter a valid email address.");
                } else {
                    emailLayout.setError(null);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = password.getText().toString();
                if (text.length() < 6) {
                    passwordLayout.setError("Error: Password must be longer than 5 characters.");
                } else {
                    passwordLayout.setError(null);
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String confirmPasswordString = confirmPassword.getText().toString();
                String passwordString = password.getText().toString();
                if (!confirmPasswordString.equals(passwordString)) {
                    confirmPasswordLayout.setError("Error: Passwords do not match.");
                } else {
                    confirmPasswordLayout.setError(null);
                }
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorFree = (emailLayout.getError() == null)
                        && (passwordLayout.getError() == null)
                        && (confirmPasswordLayout.getError() == null);

                boolean filledOut = (email.getText().length() > 0)
                        && (password.getText().length() > 0)
                        && (confirmPassword.getText().length() > 0);

                if (!errorFree) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpPage.this);
                    builder.setTitle("Sign Up Error");
                    builder.setMessage("Please fix the indicated errors in your sign up form to continue.");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else if (!filledOut) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpPage.this);
                    builder.setTitle("Sign Up Error");
                    builder.setMessage("Please complete all fields of the sign up form to continue.");
                    String okText = "ok";
                    builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mDatabase.getReference().child("ProfileNotComplete").child(mAuth.getCurrentUser().getUid()).setValue(true);
                            Intent intent = new Intent(SignUpPage.this, SignUpChoicePage.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }
}
