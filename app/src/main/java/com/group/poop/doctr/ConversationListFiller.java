package com.group.poop.doctr;

//Use this class to fill in conversation_list (in fragment_conversation.xml) with a ConversationAdapter
public class ConversationListFiller {
    public String doctor;
    public String patient;
    public String doctorUID;
    public String patientUID;
    public String nameToShow;
    public String chatID;
    public ChatMessage last;

    public ConversationListFiller(String chatID, String doctor, String patient, String doctorUID, String patientUID, ChatMessage last){
        this.chatID = chatID;
        this.doctor = doctor;
        this.patient = patient;
        this.doctorUID = doctorUID;
        this.patientUID = patientUID;
        this.last = last;
    }

}
