package com.group.poop.doctr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MedicalRecord {
    private String apptType;
    private Date apptTime;
    private String apptDesc;
    private String doctorUID;
    private String doctorString;

    public MedicalRecord(){

    }

    MedicalRecord( String apptType, Date apptTime, String apptDesc, String doctorUID, String doctorString) {
        this.apptType = apptType;
        this.apptTime = apptTime;
        this.apptDesc = apptDesc;
        this.doctorUID = doctorUID;
        this.doctorString = doctorString;
    }

    public String getDoctorUID() {return doctorUID;}

    public String getDoctorString() {return doctorString;}

    public String getApptType() {
        return apptType;
    }

    public Date getApptTime() {
        return apptTime;
    }

    public String getApptTimeString(){
        // TODO - Return formatted dd/mm/yyyy
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        if( apptTime != null ) {
            return format.format(apptTime);
        }
        return "MM/dd/yyyy";
    }

    public String getApptDesc() {
        return apptDesc;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public void setApptTime(Date apptTime) {
        this.apptTime = apptTime;
    }

    public void setApptDesc(String apptDesc) {
        this.apptDesc = apptDesc;
    }
}
