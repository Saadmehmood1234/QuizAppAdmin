package com.quizappadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
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


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, you want to delete this category");

                builder.setPositiveButton("Yes",(dialogInterface, i) -> {

                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                            .child("questions").child(questionModel.getKey())
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();

                                }
                            });

                });
                builder.setNegativeButton("No",(dialogInterface, i) -> {

                    dialogInterface.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();



                return false;
            }
        });

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
