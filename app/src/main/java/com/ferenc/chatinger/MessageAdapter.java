package com.ferenc.chatinger;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModell> msgAdapterList;

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;


    public MessageAdapter(Context context, ArrayList<MessageModell> msgAdapterList) {
        this.context = context;
        this.msgAdapterList = msgAdapterList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.partner_layout, parent, false);
            return new PartnerViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModell messages = msgAdapterList.get(position);

        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.message.setText(messages.getMessage());
        }
        else{
            PartnerViewHolder viewHolder = (PartnerViewHolder) holder;
            viewHolder.message.setText(messages.getMessage());
        }

    }

    public int getItemViewType(int position){
        MessageModell messages = msgAdapterList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderID())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class SenderViewHolder extends RecyclerView.ViewHolder {

    TextView partnerName;
    TextView message;

    public SenderViewHolder(@NonNull View itemView) {
        super(itemView);

        message = itemView.findViewById(R.id.senderText);
    }
}

class PartnerViewHolder extends RecyclerView.ViewHolder {

    TextView partnerName;
    TextView message;

    public PartnerViewHolder(@NonNull View itemView) {
        super(itemView);

        message = itemView.findViewById(R.id.partnerText);
    }
}
