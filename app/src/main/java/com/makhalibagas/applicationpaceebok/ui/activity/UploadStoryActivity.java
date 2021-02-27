package com.makhalibagas.applicationpaceebok.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makhalibagas.applicationpaceebok.R;

import java.util.HashMap;

public class UploadStoryActivity extends AppCompatActivity {

    private EditText etStory;
    private ImageView imgStory;
    private Uri uriImageStory;

    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_story);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        initView();
    }


    private void initView(){
        etStory = findViewById(R.id.etEditStory);
        imgStory = findViewById(R.id.imgStory);
        imgStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        findViewById(R.id.shareNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStory.getText().toString().isEmpty()){
                    Toast.makeText(UploadStoryActivity.this, "please type to story", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageStory");
                    final StorageReference imagePath = storageReference.child(uriImageStory.getLastPathSegment());
                    imagePath.putFile(uriImageStory).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Story").child(firebaseUser.getUid()).push();
                                    long timeEnd = System.currentTimeMillis()+86400000;


                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("contentStory", etStory.getText().toString());
                                    hashMap.put("imageStory", uri.toString());
                                    hashMap.put("publisherStory", firebaseUser.getUid());
                                    hashMap.put("keyStory", databaseReference.getKey());
                                    hashMap.put("timeEnd", timeEnd);
                                    hashMap.put("timeStart", ServerValue.TIMESTAMP);
                                    databaseReference.setValue(hashMap);

                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });

                }
            }
        });

    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null){
            uriImageStory = data.getData();
            imgStory.setImageURI(uriImageStory);
        }
    }
}
