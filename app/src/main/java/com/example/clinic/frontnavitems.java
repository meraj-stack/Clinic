package com.example.clinic;

import java.util.List;

public class frontnavitems {
    private String title;
    private int icon;
    private List<frontsubitems> frontsubitems;

    public frontnavitems(String title, int icon, List<frontsubitems> frontsubitems){
        this.title=title;
        this.icon=icon;
        this.frontsubitems=frontsubitems;
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

    public List<com.example.clinic.frontsubitems> getFrontsubitems() {
        return frontsubitems;
    }

    public void setFrontsubitems(List<com.example.clinic.frontsubitems> frontsubitems) {
        this.frontsubitems = frontsubitems;
    }
}
