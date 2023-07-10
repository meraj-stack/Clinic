package com.example.clinic;

import java.util.List;

public class docnavitems {
    private String title;
    private int icon;
    List<docsubitems> docsubitems;

    public  docnavitems (String title, int icon, List<docsubitems> docsubitems){
        this.title = title;
        this.icon=icon;
        this.docsubitems=docsubitems;
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

    public List<com.example.clinic.docsubitems> getDocsubitems() {
        return docsubitems;
    }

    public void setDocsubitems(List<com.example.clinic.docsubitems> docsubitems) {
        this.docsubitems = docsubitems;
    }
}
