package com.example.clinic;

public class ClinicUser {
    String firstname;
    String lastname;
    String gender;
    String dob;
    String contact;
    String city;
    String country;
    String language;
    String usertype;
    String specialization;
    String address;
    String bio;
    String imageurl;

    public ClinicUser(String firstname, String lastname, String gender, String dob, String contact, String city, String country, String language, String usertype, String specialization,  String address, String bio, String imageurl){
        this.firstname=firstname;
        this.lastname=lastname;
        this.gender=gender;
        this.dob=dob;
        this.contact=contact;
        this.city=city;
        this.country=country;
        this.language=language;
        this.usertype=usertype;
        this.specialization=specialization;
        this.address=address;
        this.bio=bio;
        this.imageurl=imageurl;

    }
    public ClinicUser(){

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
