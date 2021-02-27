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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.adapter.FriendsMeAdapter;
import com.makhalibagas.applicationpaceebok.adapter.PostAdapter;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.model.Post;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeActivity extends AppCompatActivity {

    private RecyclerView recyclerView, rvFriends;
    private List<Post> postList = new ArrayList<>();
    private List<Friend> userList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private TextView noPost;
    private FirebaseUser firebaseUser;
    private String userStatus;
    private Button btPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadPostActivity.class));
            }
        });


        findViewById(R.id.addStory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadStoryActivity.class));
            }
        });

        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
        noPost = findViewById(R.id.nopost);
        firebaseDatabase = FirebaseDatabase.getInstance();
        CircleImageView imgProfile = findViewById(R.id.imgProfile);
        TextView tvToolbar = findViewById(R.id.tvToolbar);
        TextView tvName = findViewById(R.id.tvName);
        Button editProfile = findViewById(R.id.editProfile);
        Button addStory = findViewById(R.id.addStory);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        CircleImageView accountImage = findViewById(R.id.account);
        Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl()).into(accountImage);
        final User user = getIntent().getParcelableExtra(ChatActivity.EXTRA_USER);
        if (user != null){
            Glide.with(getApplicationContext()).load(user.getUserImage()).into(imgProfile);
            tvToolbar.setText(user.getUserName());
            tvName.setText(user.getUserName());
            getPost(user);
            getFriends(user);
            if (user.getUserId().equals(firebaseUser.getUid())){
                editProfile.setVisibility(View.VISIBLE);
                addStory.setVisibility(View.VISIBLE);
            }else {
                editProfile.setVisibility(View.GONE);
                addStory.setVisibility(View.GONE);
            }
            findViewById(R.id.seeAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
                    intent.putExtra("IDUSER", user.getUserId());
                    startActivity(intent);
                }
            });
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUserId());
            getDetailMe(databaseReference);
            View view2 = findViewById(R.id.viewLine2);
            View view3 = findViewById(R.id.viewLine3);
            view2.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
            TextView post = findViewById(R.id.post);
            post.setVisibility(View.INVISIBLE);
            LinearLayout linearLayout = findViewById(R.id.linearPosts);
            linearLayout.setVisibility(View.INVISIBLE);
        }else {
            Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl()).into(imgProfile);
            tvToolbar.setText(firebaseUser.getDisplayName());
            tvName.setText(firebaseUser.getDisplayName());
            getUserPost();
            getUserFriends();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
            getDetailMe(databaseReference);
            findViewById(R.id.seeAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), FriendActivity.class));
                }
            });
        }
    }
    private void getUserPost(){
        recyclerView = findViewById(R.id.rvPostUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference databaseReference = firebaseDatabase.getReference("PostById").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                    noPost.setVisibility(View.INVISIBLE);
                }

                PostAdapter postAdapter = new PostAdapter(getApplicationContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getUserFriends(){
        final TextView jmlFriend = findViewById(R.id.jmlhFriends);
        rvFriends = findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new GridLayoutManager(this, 3));
        DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                jmlFriend.setText(snapshot.getChildrenCount() +" Friend" );
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend user = dataSnapshot.getValue(Friend.class);
                    if (dataSnapshot.getChildrenCount() < 6){
                        userList.add(user);
                    }
                }
                rvFriends.setAdapter(new FriendsMeAdapter(getApplicationContext(), userList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getPost(User user){
        recyclerView = findViewById(R.id.rvPostUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference databaseReference = firebaseDatabase.getReference("PostById").child(user.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                    noPost.setVisibility(View.INVISIBLE);
                }

                PostAdapter postAdapter = new PostAdapter(getApplicationContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getFriends(User user){
        final TextView jmlFriend = findViewById(R.id.jmlhFriends);
        rvFriends = findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new GridLayoutManager(this, 3));
        DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(user.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                jmlFriend.setText(snapshot.getChildrenCount() +" Friend" );
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend user = dataSnapshot.getValue(Friend.class);
                    userList.add(user);
                }
                rvFriends.setAdapter(new FriendsMeAdapter(getApplicationContext(), userList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDetailMe(DatabaseReference databaseReference){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    TextView schools = findViewById(R.id.tvSchooll);
                    TextView location = findViewById(R.id.tvLocation);
                    TextView email = findViewById(R.id.tvEmail);
                    TextView bio = findViewById(R.id.tvBio);
                    TextView number = findViewById(R.id.tvNumber);
                    schools.setText(user.getUserSchools());
                    email.setText(user.getUserEmail());
                    bio.setText(user.getUserBio());
                    number.setText(user.getUserNumber());
                    location.setText(user.getUserLocation());
                }
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
