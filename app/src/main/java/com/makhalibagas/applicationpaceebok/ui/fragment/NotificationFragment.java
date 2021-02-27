package com.makhalibagas.applicationpaceebok.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.adapter.NotificationAdapter;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private List<Notification> notifications = new ArrayList<>();
    private TextView noList;
    private List<String> friendsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noList = view.findViewById(R.id.tvNo);
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
                getNotif();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getNotif() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    for (String id : friendsList){
                        if (notification.getPublisherId().equals(id)){
                            noList.setVisibility(View.INVISIBLE);
                            notifications.add(notification);
                        }
                    }
                }

                recyclerView.setAdapter(new NotificationAdapter(getContext(), notifications));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
