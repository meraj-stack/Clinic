package com.example.clinic;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class SearchViewCursorAdapter extends CursorAdapter {
    private SearchAdapter searchAdapter;

    public SearchViewCursorAdapter(Context context, SearchAdapter searchAdapter) {
        super(context, null, false);
        this.searchAdapter = searchAdapter;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Use the same layout as your SearchAdapter
        return searchAdapter.newView(context, null, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Use the same bindView method as your SearchAdapter
        searchAdapter.bindView(view, context, cursor);
    }

    @Override
    public Cursor getCursor() {
        // Return null to prevent the framework from trying to manage a cursor
        return null;
    }
}
