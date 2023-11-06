package com.ferenc.chatinger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferenc.chatinger.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_PARTNER = 2;

    private Context context;
    private ArrayList<MessageModell> msgAdapterList;

    public MessageAdapter(Context context, ArrayList<MessageModell> msgAdapterList) {
        this.context = context;
        this.msgAdapterList = msgAdapterList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENDER) {
            view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.partner_layout, parent, false);
            return new PartnerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModell message = msgAdapterList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SENDER:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                senderViewHolder.message.setText(message.getMessage());
                break;
            case VIEW_TYPE_PARTNER:
                PartnerViewHolder partnerViewHolder = (PartnerViewHolder) holder;
                partnerViewHolder.message.setText(message.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return msgAdapterList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModell message = msgAdapterList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID())) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_PARTNER;
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.senderText);
        }
    }

    static class PartnerViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        PartnerViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.partnerText);
        }
    }
}
