package com.makhalibagas.applicationpaceebok.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.Friend;
import com.makhalibagas.applicationpaceebok.ui.activity.ChatActivity;
import com.makhalibagas.applicationpaceebok.ui.activity.MeActivity;

import java.util.List;

/**
 * Created by Bagas Makhali on 7/7/2020.
 */
public class FriendsMeAdapter extends RecyclerView.Adapter<FriendsMeAdapter.ViewHolder> {


    //adapter untuk recyclerview di meActivity
    private Context context;
    private List<Friend> userList;

    public FriendsMeAdapter(Context context, List<Friend> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendsMeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsMeAdapter.ViewHolder holder, int position) {

        final Friend user = userList.get(position);
        holder.tvNameUser.setText(user.getFriendName());
        Glide.with(context).load(user.getFriendImage()).into(holder.imageUser);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser;
        TextView tvNameUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.imgProfile);
            tvNameUser = itemView.findViewById(R.id.tvName);
        }
    }
}
