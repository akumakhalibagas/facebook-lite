package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.adapter.FriendsChatAdapter;
import com.makhalibagas.applicationpaceebok.adapter.FriendsMeAdapter;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView rvFriends;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private List<Friend> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String id = getIntent().getStringExtra("IDUSER");

        if (id != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(id);
            getUserFriends(databaseReference);
        }else {
            DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(firebaseUser.getUid());
            getUserFriends(databaseReference);
        }
    }


    private void getUserFriends(DatabaseReference databaseReference){
        rvFriends = findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend user = dataSnapshot.getValue(Friend.class);
                    userList.add(user);
                }

                rvFriends.setAdapter(new FriendsChatAdapter(getApplicationContext(), userList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
