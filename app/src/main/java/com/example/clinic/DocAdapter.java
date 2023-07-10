package com.example.clinic;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class
DocAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<docnavitems> itemList;

    public DocAdapter (Context context, List<docnavitems> itemList){
        this.context=context;
        this.itemList=itemList;
    }
    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<docsubitems> subItems = itemList.get(groupPosition).getDocsubitems();
        if(subItems!=null){
            return subItems.size();

        }else{
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).getDocsubitems().get(childPosition);
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
            convertView = inflater.inflate(R.layout.docnav_items, null);
        }

        TextView txt_itemtext = convertView.findViewById(R.id.txt_itemtext);
        txt_itemtext.setText(itemList.get(groupPosition).getTitle());
        ImageView iconleft = convertView.findViewById(R.id.iconleft);
        iconleft.setImageResource(itemList.get(groupPosition).getIcon());
        ImageView iconright = convertView.findViewById(R.id.iconright);
        int color = isExpanded ? R.color.iconpressed : R.color.iconunpressed;
        int img = isExpanded ? R.drawable.ic_baseline_downarrow : R.drawable.ic_baseline_right_arrow;
        iconright.setImageResource(img);
        iconleft.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
        txt_itemtext.setTextColor(ContextCompat.getColor(context, color));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View childView, ViewGroup parent) {
        if(childView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.docnav_subitems, null);
        }
        TextView txt_subtext = childView.findViewById(R.id.txt_subtext);
        docsubitems subitems= itemList.get(groupPosition).docsubitems.get(childPosition);
        txt_subtext.setText(subitems.getTitle());
        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
