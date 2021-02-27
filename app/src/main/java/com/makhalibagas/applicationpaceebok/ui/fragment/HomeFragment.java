package com.makhalibagas.applicationpaceebok.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.adapter.PostAdapter;
import com.makhalibagas.applicationpaceebok.adapter.StoryAdapter;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.model.Post;
import com.makhalibagas.applicationpaceebok.model.Story;
import com.makhalibagas.applicationpaceebok.ui.activity.UploadPostActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView,rvStory;
    private DatabaseReference databaseReference;
    private List<Story> storyList;
    private StoryAdapter storyAdapter;
    private List<Post> postList = new ArrayList<>();
    private List<String> friendsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.rvPost);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Post");

        view.findViewById(R.id.btPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadPostActivity.class));
            }
        });

        rvStory = view.findViewById(R.id.rvStory);
        rvStory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvStory.setHasFixedSize(true);
        storyList = new ArrayList<>();


        view.findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadPostActivity.class));
            }
        });
        CircleImageView accountImg = view.findViewById(R.id.account);
        Glide.with(Objects.requireNonNull(getContext())).load(Objects.requireNonNull(firebaseUser).getPhotoUrl()).into(accountImg);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkFriend();
    }

    private void checkFriend(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    friendsList.add(friend.getFriendId());
                }
                getPost();
                readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getPost() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    for (String idFriend : friendsList){
                        if (post.getPublisherId().equals(idFriend)){
                            postList.add(post);
                        }
                    }
                }

                PostAdapter postAdapter = new PostAdapter(getContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readStory(){
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Story");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timeCurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("","",FirebaseAuth.getInstance().getCurrentUser().getUid(),"",0,0));
                for (String id : friendsList){
                    int countStory = 0;
                    Story story = null;
                    for (DataSnapshot dataSnapshot : snapshot.child(id).getChildren()){
                        story = dataSnapshot.getValue(Story.class);
                        if (timeCurrent > story.getTimeStart() && timeCurrent < story.getTimeEnd()){
                            countStory++;
                        }
                    }

                    if (countStory > 0){
                        storyList.add(story);
                    }
                }


                storyAdapter = new StoryAdapter(getContext(), storyList);
                rvStory.setAdapter(storyAdapter);
                rvStory.setVisibility(View.VISIBLE);

                storyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
