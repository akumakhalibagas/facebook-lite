package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etBio, etSekolah, etLokasi, etNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        etBio = findViewById(R.id.etBio);
        etSekolah = findViewById(R.id.etSekolah);
        etLokasi = findViewById(R.id.etLokasi);
        etNumber = findViewById(R.id.etPhone);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                etBio.setText(user.getUserBio());
                etSekolah.setText(user.getUserSchools());
                etLokasi.setText(user.getUserLocation());
                etNumber.setText(user.getUserNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        findViewById(R.id.tvUpdateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userBio", etBio.getText().toString());
                hashMap.put("userSchools", etSekolah.getText().toString());
                hashMap.put("userLocation", etLokasi.getText().toString());
                hashMap.put("userNumber", etNumber.getText().toString());
                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Update Succes", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MeActivity.class));
                    }
                });
            }
        });
    }
}
