package com.example.clinic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.math.BigInteger;
import java.util.List;

public class NavListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<navitems> navitems;

    public NavListAdapter(Context context, List<navitems> navitems){
        this.context=context;
        this.navitems=navitems;

    }

    @Override
    public int getGroupCount() {
        return navitems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<SubNavItem> subItems = navitems.get(groupPosition).getSubItems();
        if (subItems != null) {
            return subItems.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return navitems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return navitems.get(groupPosition).getSubItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_items, null);
        }
        TextView txt_itemtext = convertView.findViewById(R.id.txt_itemtext);
        txt_itemtext.setText(navitems.get(groupPosition).getTitle());
        ImageView iconleft = convertView.findViewById(R.id.iconleft);
        iconleft.setImageResource(navitems.get(groupPosition).getIcon());
        ImageView iconright = convertView.findViewById(R.id.iconright);

        int colorres = isExpanded ? R.color.pressedcolor :R.color.unpresscolor;
        int imgres = isExpanded ? R.drawable.ic_baseline_keyboard_arrow_down_24 : R.drawable.ic_arrowright;
        iconright.setImageResource(imgres);
        iconleft.setColorFilter(ContextCompat.getColor(context, colorres), PorterDuff.Mode.SRC_IN);
        txt_itemtext.setTextColor(ContextCompat.getColor(context, colorres));
        if (navitems.get(groupPosition).getSubItems() != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (isExpanded) {
                        ((ExpandableListView) parent).collapseGroup(groupPosition);

                    } else {
                        ((ExpandableListView) parent).expandGroup(groupPosition);

                    }
                }


            });
        }
          else{
              convertView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Fragment fragment = null;
                      switch (navitems.get(groupPosition).getTitle()){
                          case "DashBoard":
                              fragment = new DashBoardFragment();
                              break;
                          case "Appointment":
                              fragment = new AppointmentFragment();
                              break;
                          case "Add Drugs":
                              fragment = new AddDrugFragment();
                              break;
                          case "Calender":
                              fragment = new CalenderFragment();
                              break;
                          case "Reports":
                              fragment = new ReportFragment();
                              break;

                      }
                      if(fragment!=null){
                          FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                          FragmentTransaction ft = fm.beginTransaction();
                          ft.replace(R.id.container, fragment);
                          ft.commit();
                      }

                  }
              });
              iconright.setImageResource(0);

        }



        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View childView, ViewGroup parent) {
        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.submenu_layout, null);
        }

        TextView txt_subtext = childView.findViewById(R.id.txt_subtext);
        LinearLayout subitems = childView.findViewById(R.id.subitems);
        SubNavItem subNavItem = navitems.get(groupPosition).getSubItems().get(childPosition);
        txt_subtext.setText(subNavItem.getTitle());
        txt_subtext.setTextColor(Color.parseColor("#000000"));

        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
