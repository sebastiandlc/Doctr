package com.group.poop.doctr;

import java.util.ArrayList;
import java.util.Date;


public class User {
    private String uid; // corresponds to Firebase Authentication unique identifier (UID)
    private String firstName;
    private String lastName;
    private Date birthday;
    private String gender;
    private Long weight; // lbs
    private Long height; // inches
    private String allergies;
    private String medications;
    private boolean showMR;

    // TODO - private Image profilePicture; figure this out later

    // Empty default constructor for Firebase to populate child nodes
    public User(){

    }

    // Constructor
    User(String uid,
         String firstName,
         String lastName,
         Date birthday,
         String gender,
         Long height,
         Long weight,
         String allergies,

         String medications
    ) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.allergies = allergies;
        this.medications = medications;
        showMR = true;
    }

    public User(String parseString)
    {

    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public Long getWeight() {
        return weight;
    }

    public Long getHeight() { return height; }

    public String getAllergies() { return allergies; }

    public String getMedications() { return medications; }

    public boolean getShowMR() {return showMR;}

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public void setShowMR(boolean showMR) {this.showMR = showMR;}
}
