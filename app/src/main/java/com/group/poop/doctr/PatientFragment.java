package com.group.poop.doctr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView patient_list;
    private ArrayList<User> users;
    private DatabaseReference ref;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance() {
        return new PatientFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        patient_list = view.findViewById(R.id.patient_frag_patient_list);

        users = new ArrayList<User>();
        String uid = FirebaseAuth.getInstance().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        patient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User user = users.get(position);

                Intent intent = new Intent(getActivity(), DoctorMedicalRecordActivity.class);
                intent.putExtra(DoctorMedicalRecordActivity.UID_PARAM, user.getUid());
                intent.putExtra(DoctorMedicalRecordActivity.SHOW_MR_PARAM, user.getShowMR());
                startActivity(intent);

            }
        });

        Query query = ref.child("DoctorsPatients").child(uid).orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getKey();

                Query query = ref.child("UserProfiles").child(uid).orderByKey();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        users.add(user);
                        PatientAdapter userAdapter = new PatientAdapter(getActivity(), users);
                        patient_list.setAdapter(userAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


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

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uid) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uid);
        }
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
        void onFragmentInteraction(String uid);
    }

    private class PatientAdapter extends ArrayAdapter<User> {

        public PatientAdapter(Context context, ArrayList<User> users) {
            super(context, R.layout.patient_card, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            User user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_card, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.patient_card_name);
            TextView dob = (TextView) convertView.findViewById(R.id.patient_card_dob);
            TextView gender = (TextView) convertView.findViewById(R.id.patient_card_gender);

            name.setText(user.getFirstName() + " " + user.getLastName());
            dob.setText(user.getBirthday().toString());
            gender.setText(user.getGender());

            return convertView;
        }
    }
}
