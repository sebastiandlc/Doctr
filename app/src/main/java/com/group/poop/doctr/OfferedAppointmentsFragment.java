package com.group.poop.doctr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferedAppointmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferedAppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferedAppointmentsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Appointment appointment = null;
    private RecyclerView mRecycler;


    public OfferedAppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment OfferedAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferedAppointmentsFragment newInstance() {
        return new OfferedAppointmentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offered_appointments, container, false);

        mRecycler = view.findViewById(R.id.offered_appointment_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<Appointment> appointments = new ArrayList<Appointment>();


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase.child("AppointmentProfiles").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                appointments.clear();

                if(snapshot.exists()) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Log.e("value " ,""+postSnapshot.getValue());
                        appointment = postSnapshot.getValue(Appointment.class);
                        Log.e("value " ,""+appointment.getDoctorName());
                        if(appointment.getAccepted() == -1 || appointment.getAccepted() == 1) {
                            continue;
                        }
                        appointments.add(appointment);
                    }
                    OfferedAppointmentAdapter oaa = new OfferedAppointmentAdapter(appointments, getActivity());
                    mRecycler.setAdapter(oaa);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
