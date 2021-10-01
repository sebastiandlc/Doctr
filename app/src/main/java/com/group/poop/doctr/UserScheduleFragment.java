package com.group.poop.doctr;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Array;
import java.text.SimpleDateFormat;
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
public class UserScheduleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayList<Appointment> appt_list;

    private String uid;

    private ListView layout_list;

    public UserScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserScheduleFragment newInstance() {
        return new UserScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appt_list = new ArrayList<Appointment>();

        uid = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_schedule, container, false);

        layout_list = view.findViewById(R.id.fragment_user_schedule_list);

        Query query = FirebaseDatabase.getInstance().getReference().child("AppointmentProfiles").orderByKey();

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot apptSnapshot: dataSnapshot.getChildren()) {
                        Appointment appointment = apptSnapshot.getValue(Appointment.class);
                        if(appointment.getPatientUID().equals(uid))
                            appt_list.add(appointment);
                    }

                    if(appt_list.size() > 0) {
                        PatientAppointmentCardAdapter adp = new PatientAppointmentCardAdapter(getActivity(), appt_list);
                        layout_list.setAdapter(adp);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot apptSnapshot: dataSnapshot.getChildren()) {
                        Appointment appointment = apptSnapshot.getValue(Appointment.class);
                        if(appointment.getPatientUID().equals(uid))
                            appt_list.add(appointment);
                    }
                    PatientAppointmentCardAdapter adp = new PatientAppointmentCardAdapter(getActivity(), appt_list);
                    layout_list.setAdapter(adp);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
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

    private class PatientAppointmentCardAdapter extends ArrayAdapter<Appointment>{

        public PatientAppointmentCardAdapter(Context context, ArrayList<Appointment> appt) {
            super(context, R.layout.patient_appotinment_card, appt);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Appointment appt = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_appotinment_card, parent, false);
            }

            TextView start = (TextView) convertView.findViewById(R.id.patient_appointment_start);
            TextView date = (TextView) convertView.findViewById(R.id.patient_appointment_date);
            TextView name = (TextView) convertView.findViewById(R.id.patient_appointment_dr_name);
            TextView price = (TextView) convertView.findViewById(R.id.patient_appointment_price);
            TextView location = (TextView) convertView.findViewById(R.id.patient_appointment_location);
            TextView specialty = (TextView) convertView.findViewById(R.id.patient_appointment_speciality);
            TextView end = (TextView) convertView.findViewById(R.id.patient_appointment_end);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String str = format.format(appt.getDate().getTime());

            date.setText(str);

            format = new SimpleDateFormat("hh:mm aaa");
            str = format.format(appt.getStartTime());

            start.setText(str);

            str = format.format(appt.getEndTime());

            end.setText(str);

            name.setText(appt.getDoctorName());
            price.setText(appt.getPrice().toString());
            location.setText(appt.getLocation());
            specialty.setText(appt.getSpeciality());
            int accepted_int = appt.getAccepted();
            if(accepted_int != 0){
                TextView accepted = (TextView)convertView.findViewById(R.id.accepted);
                if(appt.getAccepted() == -1) {
                    accepted.setText("Rejected...");
                } else {
                    accepted.setText("Accepted...");
                    accepted.setTextColor(Color.parseColor("#00f725"));
                }
            }

            return convertView;
        }
    }
}
