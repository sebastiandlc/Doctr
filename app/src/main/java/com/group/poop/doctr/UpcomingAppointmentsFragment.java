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
 * {@link UpcomingAppointmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingAppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingAppointmentsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Appointment appointment = null;
    private RecyclerView mRecycler;


    public UpcomingAppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment UpcomingAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingAppointmentsFragment newInstance() {
        return new UpcomingAppointmentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);

        mRecycler = view.findViewById(R.id.upcoming_appointment_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<Appointment> appointments = new ArrayList<Appointment>();

        Calendar cDate = Calendar.getInstance();
        cDate.set(2017, 12, 12);

        Calendar cStart = Calendar.getInstance();
        cStart.set(Calendar.HOUR_OF_DAY, 14);
        cStart.set(Calendar.MINUTE, 45);

        Calendar cEnd = Calendar.getInstance();
        cEnd.set(Calendar.HOUR_OF_DAY, 15);
        cEnd.set(Calendar.MINUTE, 45);

        Calendar cDate2 = Calendar.getInstance();
        cDate.set(2018, 1, 9);

        Calendar cStart2 = Calendar.getInstance();
        cStart.set(Calendar.HOUR_OF_DAY, 9);
        cStart.set(Calendar.MINUTE, 45);

        Calendar cEnd2 = Calendar.getInstance();
        cEnd.set(Calendar.HOUR_OF_DAY, 13);
        cEnd.set(Calendar.MINUTE, 0);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        mDatabase.child("AppointmentProfiles").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Log.e("value " ,""+postSnapshot.getValue());
                        appointment = postSnapshot.getValue(Appointment.class);
                        Log.e("value " ,""+appointment.getDoctorName());
                        if(appointment.getPatientUID() == "NULL" || appointment.getPatientUID().equals("NULL")){
                            continue;
                        }
                        if(appointment.getAccepted()== 1) {
                            appointments.add(appointment);
                        }
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
