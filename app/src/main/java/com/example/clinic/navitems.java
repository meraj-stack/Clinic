package com.example.clinic;

import java.util.List;

public class navitems {

    private String title;
    private int icon;
    private List<SubNavItem> subItems;


    public navitems(String title, int icon, List<SubNavItem> subItems) {
        this.title = title;
        this.icon = icon;
        this.subItems = subItems;
    }


    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public List<SubNavItem> getSubItems() {
        return subItems;
    }

    public boolean hasSubItems() {
        return subItems != null && subItems.size() > 0;
    }
}
