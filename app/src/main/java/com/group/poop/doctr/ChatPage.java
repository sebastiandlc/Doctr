
package com.group.poop.doctr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatPage extends AppCompatActivity {

    static final String CHAT_ID_PARAM = "chatid";

    private String chatId;

    private FirebaseAuth mAuth;

    private DatabaseReference ref;

    private FirebaseUser user;

    private String userName;

    private ArrayList<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String recipient = "DUMMY";
        Bundle b = getIntent().getExtras();
        if(b != null)
            chatId = b.getString(CHAT_ID_PARAM);

        if(chatId == null)
            ;//TODO error

        setContentView(R.layout.activity_chat_page);
        mAuth = FirebaseAuth.getInstance();

        if((user = mAuth.getCurrentUser()) == null){
            //TODO: go to login activity
        }

        userName = "Error";

        Query query1 = FirebaseDatabase.getInstance().getReference().child("Conversations").child(chatId).orderByKey();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Conversation conv = dataSnapshot.getValue(Conversation.class);
                    if(user.getUid().equals(conv.getPatientUID()))
                        userName = conv.getPatientName();
                    else
                        userName = conv.getDoctorName();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("Messages").child(chatId);

        messages = new ArrayList<ChatMessage>();

        Query query = ref.orderByChild("timeStamp");

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                    messages.add(msg);

                  displayMessages();
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
        } );

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                ref.push().setValue(new ChatMessage(input.getText().toString(),userName, user.getUid()));

                // Clear the input
                input.setText("");
            }
        });
        displayMessages();
    }

    private void displayMessages() {
        ListView messages_list = (ListView)findViewById(R.id.messages_list);

        ChatAdapter adapter = new ChatAdapter(this, messages);

        messages_list.setAdapter(adapter);
    }

    private class ChatAdapter extends ArrayAdapter<ChatMessage> {

        public ChatAdapter(Context context, ArrayList<ChatMessage> message) {
            super(context, R.layout.message_test, message);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ChatMessage message = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_test, parent, false);
            }

            TextView user = (TextView) convertView.findViewById(R.id.message_user);
            TextView time = (TextView) convertView.findViewById(R.id.message_time);
            TextView content = (TextView) convertView.findViewById(R.id.message_text);

            user.setText(message.getAuthor());
            time.setText(new Date(message.getTime()).toString());
            content.setText(message.getContent());

            return convertView;
        }
    }

}
