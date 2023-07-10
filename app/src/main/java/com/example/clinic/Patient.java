package com.example.clinic;

public class Patient {
    String uid;
    String firstname;
    String lastname;
    String mobile;
    String email;
    String dob;
    String sex;
    String bloodGroup;
    String maritalStatus;
    double height;
    double weight;
    String address;
    String patientHistory;
    String profile;

    public Patient(String firstname, String lastname, String mobile, String email, String dob, String sex,String bloodGroup, String maritalStatus, double height, double weight, String address, String patientHistory, String profile){
        this.firstname=firstname;
        this.lastname=lastname;
        this.mobile=mobile;
        this.email=email;
        this.dob=dob;
        this.sex= sex;
        this.bloodGroup=bloodGroup;
        this.maritalStatus=maritalStatus;
        this.height=height;
        this.weight=weight;
        this.address=address;
        this.patientHistory=patientHistory;
        this.profile=profile;
    }

    public Patient(){

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatientHistory() {
        return patientHistory;
    }

    public void setPatientHistory(String patientHistory) {
        this.patientHistory = patientHistory;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
