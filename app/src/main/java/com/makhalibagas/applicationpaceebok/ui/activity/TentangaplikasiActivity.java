package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makhalibagas.applicationpaceebok.R;

import java.util.HashMap;

public class TentangaplikasiActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentangaplikasi);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.author).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/makhalibagas")));
            }
        });
    }

    private void userStatus(String userStatus){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userStatus", userStatus);
        databaseReference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        userStatus = "Online";
        userStatus(userStatus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userStatus = "Offline";
        userStatus(userStatus);
    }
}
