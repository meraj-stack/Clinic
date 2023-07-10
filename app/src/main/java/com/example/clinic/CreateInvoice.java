package com.example.clinic;

public class CreateInvoice {
    private String patientname;
    private String doctorfee;
    private String doctorname;
    private String paymentmode;
    private String paymentstatus;
    private String date;

    public CreateInvoice(String patientname, String doctorfee, String doctorname, String paymentmode, String paymentstatus, String date){
        this.patientname = patientname;
        this.doctorfee = doctorfee;
        this.doctorname = doctorname;
        this.paymentmode = paymentmode;
        this.paymentstatus = paymentstatus;
        this.date= date;
    }
    public CreateInvoice(){

    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getDoctorfee() {
        return doctorfee;
    }

    public void setDoctorfee(String doctorfee) {
        this.doctorfee = doctorfee;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
