package com.example.clinic;

public class Appointment {
    private String Id;
    private String pateintname;
    private String Drname;
    private String Time;
    private String Date;
    private String status;

    public Appointment(String pateintname, String Drname, String Time, String Date, String status) {
        this.pateintname = pateintname;
        this.Drname = Drname;
        this.Time = Time;
        this.Date = Date;
        this.status = status;
    }

    public Appointment() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPateintname() {
        return pateintname;
    }

    public void setPateintname(String pateintname) {
        this.pateintname = pateintname;
    }

    public String getDrname() {
        return Drname;
    }

    public void setDrname(String drname) {
        Drname = drname;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
