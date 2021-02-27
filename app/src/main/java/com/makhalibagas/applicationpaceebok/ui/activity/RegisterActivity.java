package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.User;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private EditText etName, etNumber,etEmail, etBio,etPassword, etSchools, etLocation;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pictUriImage;
    private CircleImageView imageProfile;
    private FirebaseUser firebaseUser;
    private String userStatus = "Offline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btRegister).setOnClickListener(this);
        findViewById(R.id.tvMasuk).setOnClickListener(this);
        etName = findViewById(R.id.editTextNama);
        etNumber = findViewById(R.id.editTextTanggalLahir);
        etEmail = findViewById(R.id.editTextEmail);
        etBio = findViewById(R.id.editTextBio);
        etSchools = findViewById(R.id.editTextSekolah);
        etLocation = findViewById(R.id.editTextLokasi);
        etPassword = findViewById(R.id.editTextPassword);
        imageProfile = findViewById(R.id.image_profile);
        imageProfile.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvMasuk:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.image_profile:
                openGallery();
                break;
            case R.id.btRegister:
                createUser();
                break;
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){

            pictUriImage = data.getData();
            imageProfile.setImageURI(pictUriImage);
        }
    }
    private void createUser() {
        final String name = etName.getText().toString();
        final String number = etNumber.getText().toString();
        final String email =  etEmail.getText().toString();
        final String bio = etBio.getText().toString();
        final String password = etPassword.getText().toString();
        final String schools = etSchools.getText().toString();
        final String location = etLocation.getText().toString();

        //jika edit text kosong maka akan memunculkan peringatan toast
        if (name.isEmpty() || number.isEmpty()
                || bio.isEmpty() || email.isEmpty()
                || schools.isEmpty() || location.isEmpty()
                || password.isEmpty() ){
            Toast.makeText(this, "please fill in all", Toast.LENGTH_SHORT).show();
        }else {
            //jika pickUriImage tidak sam dengan null maka akan membuat akun
            if (pictUriImage != null){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null){
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageProfile");
                                final StorageReference ImageFilePath = storageReference.child(Objects.requireNonNull(pictUriImage.getLastPathSegment()));
                                ImageFilePath.putFile(pictUriImage)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                ImageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(final Uri uri) {
                                                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri)
                                                                .build();
                                                        firebaseUser.updateProfile(profileUpdate)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                                                                            User user = new User(name, number, email, bio, databaseReference.getKey(), uri.toString(), password, firebaseUser.getUid(), schools, location, userStatus);
                                                                            databaseReference.setValue(user)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            progressDialog.hide();
                                                                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                                            finish();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        });
                            }
                        }else {
                            //jika pada pembuatan akun ada yang kurang maka akan memunculkan peringatan yang kurang
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
                progressDialog.show();
            }else {
                //jika uriPictImage null maka akan menunculkan toast peringatan untuk mengisi image dahulu
                Toast.makeText(this, "Add Profile Picture first", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
