package com.quizappadmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quizappadmin.Adapters.CategoryAdapter;
import com.quizappadmin.Models.CategoryModel;
import com.quizappadmin.databinding.ActivityMainBinding;
import com.quizappadmin.databinding.ActivitySubCategoryBinding;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    ActivitySubCategoryBinding binding;
    FirebaseDatabase database;
    CategoryAdapter adapter;
    ArrayList<CategoryModel> list;
    Dialog loadingDialog;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySubCategoryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        database = FirebaseDatabase .getInstance();
        categoryId = getIntent().getStringExtra("catId");
        list = new ArrayList<>();


        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.rvCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this,list);
        binding.rvCategory.setAdapter(adapter);

        database.getReference().child("categories").child(categoryId).child("subCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(SubCategoryActivity.this, "category not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loadingDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SubCategoryActivity.this,UploadSubCategoryActivity.class);
                intent.putExtra("catId",categoryId);
                startActivity(intent);
            }
        });
    }
}