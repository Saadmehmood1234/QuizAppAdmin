package com.quizappadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizappadmin.Models.QuestionsModel;
import com.quizappadmin.Models.SubCategoryModel;
import com.quizappadmin.QuestionsActivity;
import com.quizappadmin.R;
import com.quizappadmin.databinding.RvSubcategoryDesignBinding;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewHolder> {
    Context context;
    ArrayList<QuestionsModel> list;
    private String catId;
  private String subCatId;

    public QuestionAdapter(Context context, ArrayList<QuestionsModel> list, String catId, String subCatId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        QuestionsModel questionModel = list.get(position);

        holder.binding.subCategoryName.setText(questionModel.getQuestion());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        RvSubcategoryDesignBinding binding;
    public viewHolder(@NonNull View itemView){

        super(itemView);
        binding = RvSubcategoryDesignBinding.bind(itemView);
    }
}
}
