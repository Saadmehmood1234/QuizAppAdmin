package com.quizappadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizappadmin.Models.CategoryModel;
import com.quizappadmin.R;
import com.quizappadmin.SubCategoryActivity;
import com.quizappadmin.databinding.RvCategoryDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    Context context;
    ArrayList<CategoryModel> list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_category_design, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryModel categoryModel = list.get(position);

        holder.binding.categoryName.setText(categoryModel.getCategoryName());

        Picasso.get()
                .load(categoryModel.getCategoryImage())
                .placeholder(R.drawable.admin1)
                .into(holder.binding.categoryImages);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,SubCategoryActivity.class);
                intent.putExtra("catId",categoryModel.getKey());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        RvCategoryDesignBinding binding;
    public viewHolder(@NonNull View itemView){

        super(itemView);
        binding = RvCategoryDesignBinding.bind(itemView);
    }
}
}
