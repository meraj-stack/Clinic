package com.example.clinic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private List<String> suggestionList;

    public SuggestionAdapter(List<String> suggestionList) {
        this.suggestionList = suggestionList;
    }
    @NonNull
    @Override
    public SuggestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdapter.ViewHolder holder, int position) {
        String suggestion = suggestionList.get(position);
        holder.suggestionText.setText(suggestion);


    }

    @Override
    public int getItemCount() {
        return suggestionList.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestionText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            suggestionText = itemView.findViewById(R.id.suggestion_text);
        }
    }
}
