package com.example.clinic;

public class Doctor {
    private String uid;
    private String firstname;
    private String lastname;
    private String specialization;
    private String image;

    public Doctor(String firstname, String lastname, String specialization, String image) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialization = specialization;
        this.image = image;
    }
    public Doctor(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getImage() {
        return image;
    }
}
