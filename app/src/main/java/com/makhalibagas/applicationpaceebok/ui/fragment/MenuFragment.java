package com.makhalibagas.applicationpaceebok.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.ui.activity.FriendActivity;
import com.makhalibagas.applicationpaceebok.ui.activity.LikesActivity;
import com.makhalibagas.applicationpaceebok.ui.activity.LoginActivity;
import com.makhalibagas.applicationpaceebok.ui.activity.TentangaplikasiActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    public MenuFragment() {
        // Required empty public constructor
    }

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        CircleImageView imageView = view.findViewById(R.id.imageUser);
        TextView textView = view.findViewById(R.id.tvNameUser);
        Glide.with(getContext()).load(firebaseUser.getPhotoUrl()).into(imageView);
        textView.setText(firebaseUser.getDisplayName());

        view.findViewById(R.id.btOut).setOnClickListener(this);
        view.findViewById(R.id.btPostLke).setOnClickListener(this);
        view.findViewById(R.id.btFriend).setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btOut:
                firebaseAuth.signOut();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userStatus", "Offline");
                databaseReference.updateChildren(hashMap);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btPostLke:
                startActivity(new Intent(getContext(), LikesActivity.class));
                break;
            case R.id.btFriend:
                startActivity(new Intent(getContext(), FriendActivity.class));
                break;
            case R.id.btAbout:
                startActivity(new Intent(getContext(), TentangaplikasiActivity.class));
                break;
        }
    }
}
