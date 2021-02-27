package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makhalibagas.applicationpaceebok.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etEmail, etPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermission();
        findViewById(R.id.tvDaftar).setOnClickListener(this);
        findViewById(R.id.btLogin).setOnClickListener(this);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        firebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvDaftar:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.btLogin:


                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(this, "please fill in all", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    signIn(email, password);
                }
                break;
        }
    }

    private void signIn(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.hide();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    private void checkPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Select allow to take pictures", Toast.LENGTH_SHORT).show();
            }else {
                int ID_REQUEST_IMAGE = 1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ID_REQUEST_IMAGE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
