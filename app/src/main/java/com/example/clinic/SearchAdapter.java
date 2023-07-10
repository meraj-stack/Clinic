package com.example.clinic;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends CursorAdapter {
    private final List<String> suggestions;

    public SearchAdapter(Context context, List<String> suggestions) {
        super(context, null, 0);
        this.suggestions = suggestions;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(suggestions.get(cursor.getPosition()));

    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return null; // not used, so return null
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }
}
