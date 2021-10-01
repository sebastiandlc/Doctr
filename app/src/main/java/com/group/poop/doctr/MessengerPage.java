package com.group.poop.doctr;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.List;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessengerPage extends AppCompatActivity {
/*
    private DataBase dataBase;

    private FirebaseAuth mAuth;

    private FirebaseListAdapter<ChatMessage> adapter;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            //TODO: go to login activity
        }

        ref = FirebaseDatabase.getInstance().getReference().child("UserProfiles");

    }

    private void displayDoctors(){
        ListView messages = (ListView)findViewById(R.id.messages_list);

        adapter =  new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.conversation, ref){
            @Override
            protected void populateView(View v, ChatMessage model, int position){

            }
        };
        messages.setAdapter(adapter);
    };
    */
}

