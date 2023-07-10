package com.example.clinic;

import android.hardware.lights.LightsManager;

import java.util.List;

public class adnavitems {
    private String title;
    private int icon;
    List<adsubitems> adsubitems;

    public adnavitems (String title, int icon, List<adsubitems> adsubitems){
        this.title=title;
        this.icon=icon;
        this.adsubitems=adsubitems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public List<com.example.clinic.adsubitems> getAdsubitems() {
        return adsubitems;
    }

    public void setAdsubitems(List<com.example.clinic.adsubitems> adsubitems) {
        this.adsubitems = adsubitems;
    }
}
