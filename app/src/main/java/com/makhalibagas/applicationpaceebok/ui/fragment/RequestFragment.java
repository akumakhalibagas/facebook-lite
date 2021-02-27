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
import com.makhalibagas.applicationpaceebok.adapter.FriendsRequestAdapter;
import com.makhalibagas.applicationpaceebok.model.FriendRequest;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private List<FriendRequest> userList = new ArrayList<>();
    private TextView noList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rvUserRequest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noList = view.findViewById(R.id.tvNo);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FriendRequest friendRequest = dataSnapshot.getValue(FriendRequest.class);
                    if (firebaseUser.getUid().equals(friendRequest.getRequestTo())){
                        noList.setVisibility(View.INVISIBLE);
                        userList.add(friendRequest);
                    }
                }

                recyclerView.setAdapter(new FriendsRequestAdapter(getContext(), userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
