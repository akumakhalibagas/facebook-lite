package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.adapter.ChatAdapter;
import com.makhalibagas.applicationpaceebok.model.Chat;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";

    private EditText etMessage;
    private Button btSend;
    private FirebaseUser firebaseUser;
    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        etMessage = findViewById(R.id.etChat);
        btSend = findViewById(R.id.btSend);
        recyclerView = findViewById(R.id.rvChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        setData();

    }


    private void setData(){
        final Friend user = getIntent().getParcelableExtra(EXTRA_USER);
        if (user != null){
            ImageView imageUser = findViewById(R.id.imageUser);
            Glide.with(getApplicationContext()).load(user.getFriendImage()).into(imageUser);
            TextView name = findViewById(R.id.tvNameUser);
            name.setText(user.getFriendName());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getFriendId());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userDetail = snapshot.getValue(User.class);
                    TextView status = findViewById(R.id.tvUserStatus);
                    status.setText(userDetail.getUserStatus());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etMessage.getText().toString().isEmpty()){
                        Toast.makeText(ChatActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                    }else {
                        createChat(user);
                    }
                }
            });

            getChatList(user);
        }
    }

    private void createChat(Friend user) {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Chat").push();
        Chat chat = new Chat(firebaseUser.getUid(), user.getFriendId(), etMessage.getText().toString());
        firebaseDatabase.setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etMessage.setText("");
            }
        });
    }

    private void getChatList(final Friend user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat != null){
                        if (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(user.getFriendId()) || chat.getSender().equals(user.getFriendId()) && chat.getReceiver().equals(firebaseUser.getUid()) ){
                            chatList.add(chat);
                        }
                    }
                }
                recyclerView.setAdapter(new ChatAdapter(getApplicationContext(), chatList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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