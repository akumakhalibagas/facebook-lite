package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.User;
import com.makhalibagas.applicationpaceebok.ui.fragment.MenuFragment;
import com.makhalibagas.applicationpaceebok.ui.fragment.ChatFragment;
import com.makhalibagas.applicationpaceebok.ui.fragment.HomeFragment;
import com.makhalibagas.applicationpaceebok.ui.fragment.NotificationFragment;
import com.makhalibagas.applicationpaceebok.ui.fragment.RequestFragment;
import com.makhalibagas.applicationpaceebok.ui.fragment.VideoFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long mBackPressed;
    private String userStatus;
    private FirebaseUser firebaseUser;
    private ImageView home, people, chat, video, notif, menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeActivity.class));
            }
        });
        findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadStoryActivity.class));
            }
        });

        home = findViewById(R.id.home);
        people = findViewById(R.id.people);
        chat = findViewById(R.id.chat);
        video = findViewById(R.id.video);
        notif = findViewById(R.id.notif);
        menu = findViewById(R.id.menu);
        home.setImageResource(R.drawable.ic_home2);
        home.setOnClickListener(this);
        people.setOnClickListener(this);
        chat.setOnClickListener(this);
        video.setOnClickListener(this);
        notif.setOnClickListener(this);
        menu.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {

        //why use onClick because use bottom max icon 5 and i use 6 icon for navigation
        switch (v.getId()){
            case R.id.btSearch:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
            case R.id.home:
                loadFragment(new HomeFragment());
                home.setImageResource(R.drawable.ic_home2);
                people.setImageResource(R.drawable.ic_people);
                chat.setImageResource(R.drawable.ic_chat);
                video.setImageResource(R.drawable.ic_play);
                notif.setImageResource(R.drawable.ic_notifications);
                menu.setImageResource(R.drawable.ic_menu);
                break;
            case R.id.people:
                loadFragment(new RequestFragment());
                home.setImageResource(R.drawable.ic_home);
                people.setImageResource(R.drawable.ic_people2);
                chat.setImageResource(R.drawable.ic_chat);
                video.setImageResource(R.drawable.ic_play);
                notif.setImageResource(R.drawable.ic_notifications);
                menu.setImageResource(R.drawable.ic_menu);
                break;
            case R.id.chat:
                loadFragment(new ChatFragment());
                chat.setImageResource(R.drawable.ic_chat2);
                home.setImageResource(R.drawable.ic_home);
                people.setImageResource(R.drawable.ic_people);
                video.setImageResource(R.drawable.ic_play);
                notif.setImageResource(R.drawable.ic_notifications);
                menu.setImageResource(R.drawable.ic_menu);
                break;
            case R.id.video:
                loadFragment(new VideoFragment());
                home.setImageResource(R.drawable.ic_home);
                people.setImageResource(R.drawable.ic_people);
                chat.setImageResource(R.drawable.ic_chat);
                video.setImageResource(R.drawable.ic_play2);
                notif.setImageResource(R.drawable.ic_notifications);
                menu.setImageResource(R.drawable.ic_menu);
                break;
            case R.id.notif:
                loadFragment(new NotificationFragment());
                home.setImageResource(R.drawable.ic_home);
                people.setImageResource(R.drawable.ic_people);
                chat.setImageResource(R.drawable.ic_chat);
                video.setImageResource(R.drawable.ic_play);
                notif.setImageResource(R.drawable.ic_notifications2);
                menu.setImageResource(R.drawable.ic_menu);
                break;
            case R.id.menu:
                loadFragment(new MenuFragment());
                home.setImageResource(R.drawable.ic_home);
                people.setImageResource(R.drawable.ic_people);
                chat.setImageResource(R.drawable.ic_chat);
                video.setImageResource(R.drawable.ic_play);
                notif.setImageResource(R.drawable.ic_notifications);
                menu.setImageResource(R.drawable.ic_menu2);
                break;
        }
    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
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
