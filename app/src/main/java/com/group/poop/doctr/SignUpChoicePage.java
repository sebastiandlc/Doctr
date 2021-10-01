package com.group.poop.doctr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpChoicePage extends AppCompatActivity {

    private Button mUser;
    private Button mDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choice_page);

        mUser = findViewById(R.id.user);
        mDoctor = findViewById(R.id.doctor);

        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpChoicePage.this, UserRegistration.class);
                startActivity(intent);
            }
        });

        mDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpChoicePage.this);
                builder.setTitle("Attention");
                builder.setMessage("Are you sure you wish to sign up as a doctor?");
                String okText = "yes";
                builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SignUpChoicePage.this, DoctorRegistration.class);
                        final String EXTRA_MESSAGE = "finishButtonLable";
                        String finishButtonLable = "Register";
                        intent.putExtra(EXTRA_MESSAGE, finishButtonLable);
                        startActivity(intent);
                    }
                });
                String cancelText = "cancel";
                builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO set intent to intermediate page that lets them know the need to contact support
                    }
                });
                builder.create().show();
            }
        });

    }
}
