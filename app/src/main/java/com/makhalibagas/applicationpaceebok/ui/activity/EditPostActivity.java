package com.makhalibagas.applicationpaceebok.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.Post;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    private EditText etUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        etUpdate = findViewById(R.id.etUpdatePost);
        TextView tvUpdate = findViewById(R.id.tvUpdatePost);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final Post post = getIntent().getParcelableExtra("POST");
        if (post != null){
            etUpdate.setText(post.getPost());
            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(post.getKey());
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("PostById").child(firebaseUser.getUid()).child(post.getKey());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("post", etUpdate.getText().toString());
                    databaseReference.updateChildren(hashMap);
                    database.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onBackPressed();
                        }
                    });
                }
            });
        }
    }
}
