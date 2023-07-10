package com.example.clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<Test> testList;
    private Context context;

    public TestAdapter(Context context, List<Test> testList){
        this.context=context;
        this.testList=testList;
    }
    @NonNull
    @Override
    public TestAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.txt_testname.setText(test.getTestname());
        holder.txt_testdescription.setText(test.getTestdescription());

    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{
        TextView txt_testname, txt_testdescription;


        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_testname = itemView.findViewById(R.id.txt_testname);
            txt_testdescription = itemView.findViewById(R.id.txt_testdescription);
        }
    }
}
