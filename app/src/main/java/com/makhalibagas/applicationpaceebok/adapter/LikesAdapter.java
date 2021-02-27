package com.makhalibagas.applicationpaceebok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makhalibagas.applicationpaceebok.R;
import com.makhalibagas.applicationpaceebok.model.User;

import java.util.List;

/**
 * Created by Bagas Makhali on 7/7/2020.
 */
public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    //adapter untuk recyclerview di reactedActivity
    private Context context;
    private List<User> userList;

    public LikesAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_likes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikesAdapter.ViewHolder holder, int position) {

        User user = userList.get(position);
        holder.tvNameUser.setText(user.getUserName());
        Glide.with(context).load(user.getUserImage()).into(holder.imageUser);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser;
        TextView tvNameUser;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.imageUser);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
        }
    }
}
