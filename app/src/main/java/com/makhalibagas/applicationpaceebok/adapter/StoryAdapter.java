package com.makhalibagas.applicationpaceebok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.Story;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bagas Makhali on 9/28/2020
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {


    private Context context;
    private List<Story> storyList ;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_story, parent, false));
        }else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryAdapter.ViewHolder holder, int position) {

        Story story = storyList.get(position);
        userInfo(holder, story.getPublisherStory(), position);
        Glide.with(context).load(story.getImageStory()).into(holder.imgStory);
        if (holder.getAdapterPosition() != 0){
            seenStory(holder, story.getPublisherStory());
        }

        if (holder.getAdapterPosition() == 0){
            myStory(false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0){
                    myStory(true);
                }else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private RoundedImageView imgStory;
        private CircleImageView imgAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvName);
            imgStory = itemView.findViewById(R.id.imgStory);
            imgAccount = itemView.findViewById(R.id.imgAccount);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }

        return 1;
    }


    private void userInfo(final ViewHolder viewHolder, String userid, final int pos){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getUserImage()).into(viewHolder.imgAccount);
                viewHolder.username.setText(user.getUserName());
                if (pos != 0){
                    Glide.with(context).load(user.getUserImage()).into(viewHolder.imgAccount);
                    viewHolder.username.setText(user.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myStory(final boolean click){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                long timeCurrent = System.currentTimeMillis();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Story story = dataSnapshot.getValue(Story.class);
                    if (timeCurrent > story.getTimeStart() && timeCurrent < story.getTimeEnd()){
                        count++;
                    }
                }

                if (click){
                    //TODO: add
                }else {
                    if (count > 0){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenStory(ViewHolder viewHolder, String userId){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Story")
                .child(userId);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (!dataSnapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() &&
                    System.currentTimeMillis() < dataSnapshot.getValue(Story.class).getTimeEnd());

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
