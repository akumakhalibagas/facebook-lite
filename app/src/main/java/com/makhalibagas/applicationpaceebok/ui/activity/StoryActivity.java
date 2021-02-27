package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makhalibagas.applicationpaceebok.R;

import java.util.HashMap;

public class StoryActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
