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
import com.quizappadmin.Adapters.QuestionAdapter;
import com.quizappadmin.Models.CategoryModel;
import com.quizappadmin.Models.QuestionsModel;
import com.quizappadmin.databinding.ActivityMainBinding;
import com.quizappadmin.databinding.ActivityQuestionsBinding;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    ActivityQuestionsBinding binding;
    FirebaseDatabase database;
    QuestionAdapter adapter;
    ArrayList<QuestionsModel> list;
    Dialog loadingDialog;
  private  String catId,subCatId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQuestionsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        catId=getIntent().getStringExtra("catId");
        subCatId=getIntent().getStringExtra("subCatId");

        database = FirebaseDatabase .getInstance();
        list = new ArrayList<>();


        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.rvQuestions.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this,list,catId,subCatId);
        binding.rvQuestions.setAdapter(adapter);

        database.getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                .child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        QuestionsModel model = dataSnapshot.getValue(QuestionsModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(QuestionsActivity.this, "category not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loadingDialog.dismiss();
                Toast.makeText(QuestionsActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QuestionsActivity.this,UploadQuestionsActivity.class);
                intent.putExtra("catId",catId);
                intent.putExtra("subCatId",subCatId);
                startActivity(intent);
            }
        });
    }
}