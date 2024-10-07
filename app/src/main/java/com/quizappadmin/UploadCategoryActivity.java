package com.quizappadmin;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quizappadmin.Models.CategoryModel;
import com.quizappadmin.databinding.ActivityUploadCategoryBinding;

import java.util.Date;

public class UploadCategoryActivity extends AppCompatActivity {
ActivityUploadCategoryBinding binding;
FirebaseDatabase database;
FirebaseStorage storage;
Dialog loadingDialog;
Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadCategoryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

      database=FirebaseDatabase.getInstance();
      storage=FirebaseStorage.getInstance();

      loadingDialog=new Dialog(this);
      loadingDialog.setContentView(R.layout.loading_dialog);
      loadingDialog.setCancelable(false);

      binding.fetchImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent();
              intent.setAction(Intent.ACTION_GET_CONTENT);
              intent.setType("image/*");
              startActivityForResult(intent,1);
          }
      });

      binding.btnUploadCat.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String categoryName=binding.editCategory.getText().toString();
              if(categoryName.isEmpty()){
                  binding.editCategory.setError("Enter Category Name");
              } else if (imageUri==null) {
                  Toast.makeText(UploadCategoryActivity.this,"Selected category image",Toast.LENGTH_SHORT).show();
              }
              else {
                  uploadData(categoryName,imageUri);
              }
          }
      });

    }

    private void uploadData(String categoryName, Uri imageUri) {

        loadingDialog.show();
        final StorageReference reference=storage.getReference().child("categoryImage")
                .child(new Date().getTime()+"");
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        CategoryModel model=new CategoryModel(categoryName,uri.toString());
                        database.getReference().child("categories")
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    Toast.makeText(UploadCategoryActivity.this,"data uploaded",Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();
                                    onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(UploadCategoryActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(data!=null){
                imageUri=data.getData();
                binding.categoryImage.setImageURI(imageUri);
            }
        }


    }
}