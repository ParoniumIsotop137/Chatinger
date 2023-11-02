package com.ferenc.chatinger;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    MainActivity mainActivity;
    ArrayList<User> users;
    public UserAdapter(MainActivity mainActivity, ArrayList<User> users) {

        this.mainActivity = mainActivity;
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewHolder holder, int position) {

        User user = users.get(position);
        holder.userName.setText(user.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ChatView.class);
                intent.putExtra("userName", user.getUserName());
                intent.putExtra("userID", user.getUserID());
                mainActivity.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txtUser_Name);
        }
    }
}



