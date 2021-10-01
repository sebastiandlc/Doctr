package com.group.poop.doctr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class ConversationFragment extends Fragment {

    private String uid;
    private DatabaseReference ref;
    private ArrayList<ConversationListFiller> conversations;
    private OnFragmentInteractionListener mListener;

    public ConversationFragment() {
        // Required empty public constructor
    }

    public static ConversationFragment newInstance() { return new ConversationFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uid = FirebaseAuth.getInstance().getUid();

        conversations = new ArrayList<ConversationListFiller>();

        ref = FirebaseDatabase.getInstance().getReference().child("Conversations");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Conversation conv = dataSnapshot.getValue(Conversation.class);

                if(conv.getDoctorUID().equals(uid) || conv.getPatientUID().equals(uid)) {
                    conversations.add(new ConversationListFiller(dataSnapshot.getKey(), conv.getDoctorName(),
                            conv.getPatientName(), conv.getDoctorUID(), conv.getPatientUID(), null));
                    Query query = FirebaseDatabase.getInstance().getReference()
                            .child("Messages").child(dataSnapshot.getKey()).orderByKey().limitToLast(1);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                            if (dataSnapshot.exists()) {
                                String from;
                                int i;

                                String chatId = dataSnapshot.getRef().getParent().getKey();

                                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                                View view = getView();

                                if (view != null && message != null) {
                                    ConversationListFiller conv = null;
                                   for(i = 0; i < conversations.size(); i++){
                                       if(conversations.get(i).chatID.equals(chatId)){
                                           conv = conversations.get(i);
                                           conversations.remove(i);
                                           conversations.add(0, conv);
                                           conv.last = message;
                                           break;
                                       }
                                   }
                                    if(conv != null && conv.last != null){
                                        if(uid.equals(conv.doctorUID))
                                            conv.nameToShow = conv.patient;
                                        else
                                            conv.nameToShow = conv.doctor;

                                        ListView listView = (ListView) view.findViewById(R.id.conversation_list);
                                        listView.setAdapter(new ConversationAdapter(getActivity(), conversations));
                                    }

                                }

                            }
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {/*TODO add errors*/}
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        ListView listView = (ListView) view.findViewById(R.id.conversation_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               ConversationListFiller conv = conversations.get(position);
                if (mListener != null) {
                    mListener.onFragmentInteraction(conv.chatID);
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String chatId);
    }
}


