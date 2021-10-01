package com.group.poop.doctr;

public class Doctor {

    private String uid;
    private String firstName;
    private String lastName;
    private String specialization;
    private String subSpecialization;

    public Doctor(String uid, String firstName, String lastName,
                  String specialization,
                  String subSpecialization){
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.subSpecialization = subSpecialization;
    }

    public Doctor(){

    }

    public Doctor(String formattedString)
    {
        if(formattedString == null ){ return; }

        formattedString = formattedString.trim();

        // Parse All Values
        this.firstName = parseValue(formattedString, "firstName");
        this.lastName = parseValue(formattedString, "lastName");
        this.uid = parseValue(formattedString, "uid");
        this.specialization = parseValue(formattedString, "specialization");
        this.subSpecialization = parseValue(formattedString, "subSpecialization");
    }

    private String parseValue(String parseString, String fieldString)
    {
        // Find the first Instance of a value
        int index = parseString.indexOf(fieldString);

        // Substring not found
        if(index < 0)
        {
            return null;
        }

        // Offset the index by fieldName and account for a "=" sign.
        index += fieldString.length() + 1;

        // Find first "," charachter
        int indexOfNextComma = parseString.indexOf(',', index);

        if( indexOfNextComma > 0 )
        {
            return parseString.substring(index, indexOfNextComma);

        }else
        {
            int indexOfNextSpecialChar = parseString.indexOf('}', index);

            if(indexOfNextSpecialChar > 0 )
            {
                return parseString.substring(index, indexOfNextSpecialChar);
            }
        }

        return null;
    }

    public String getUid() {return this.uid;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getSpecialization() {return specialization;}
    public String getSubSpecialization() {return subSpecialization;}

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setSubSpecialization(String subSpecialization) {
        this.subSpecialization = subSpecialization;
    }
}
