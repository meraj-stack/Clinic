package com.example.clinic;

public class Drugs {
    String id;
    String tradename;
    String genericname;
    String note;

    public Drugs (String tradename, String genericname, String note){
        this.tradename=tradename;
        this.genericname=genericname;
        this.note=note;
    }

    public Drugs (){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradename() {
        return tradename;
    }

    public void setTradename(String tradename) {
        this.tradename = tradename;
    }

    public String getGenericname() {
        return genericname;
    }

    public void setGenericname(String genericname) {
        this.genericname = genericname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
