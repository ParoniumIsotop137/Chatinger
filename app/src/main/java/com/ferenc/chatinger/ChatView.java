package com.ferenc.chatinger;

import static com.google.firebase.messaging.RemoteMessage.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class ChatView extends AppCompatActivity {

    String partnerID, partnerName, senderID, senderRoom, partnerRoom;
    TextView uName;
    CardView btnSend, btnBack;
    EditText message;

    FirebaseAuth fauth;
    FirebaseDatabase dBase;

    RecyclerView msgAdapter;

    ArrayList<MessageModell> messageList;

    MessageAdapter adapter;

    DatabaseReference chatReference;

    DatabaseReference partnerRef;

    private String msg;

    private NotificationSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        fauth = FirebaseAuth.getInstance();
        dBase = FirebaseDatabase.getInstance("https://chatinger-d3269-default-rtdb.europe-west1.firebasedatabase.app/");

        messageList = new ArrayList<>();

        partnerName = getIntent().getStringExtra("userName");
        partnerID = getIntent().getStringExtra("userID");
        senderID = fauth.getUid();

        adapter = new MessageAdapter(ChatView.this, messageList, partnerID);

        btnSend = findViewById(R.id.btnSendChV);
        message = findViewById(R.id.txtMessage);
        btnBack = findViewById(R.id.btnBackCHV);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        lyManager.setStackFromEnd(true);

        msgAdapter = findViewById(R.id.msgAdCHV);
        msgAdapter.setLayoutManager(lyManager);
        msgAdapter.setAdapter(adapter);


        uName = findViewById(R.id.userNameChv);
        uName.setText(partnerName);

        senderRoom = senderID + partnerID;
        partnerRoom = partnerID + senderID;

        chatReference = dBase.getReference().child("chats").child(senderRoom).child("messages");
        partnerRef = dBase.getReference().child("chats").child(partnerRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot item : snapshot.getChildren()
                ) {
                    MessageModell mdlMessages = item.getValue(MessageModell.class);
                    messageList.add(mdlMessages);
                }

                adapter.notifyDataSetChanged();
                scrollToBottom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        partnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    MessageModell lastMessage = null;
                    for (DataSnapshot item : snapshot.getChildren()) {
                        lastMessage = item.getValue(MessageModell.class);
                    }
                    if (lastMessage != null && lastMessage.getSenderID().equals(partnerID)) {
                        sender = new NotificationSender(getApplicationContext(), "Chatinger", "Du hast neue Nachrichten erhalten!");
                        sender.sendNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (TextUtils.isEmpty(message.getText().toString())) {

                        Toast.makeText(ChatView.this, "Schreibe zuerst eine Nachricht!", Toast.LENGTH_SHORT).show();

                    }
                hideKeyBoard(view);

                    msg = message.getText().toString();
                    message.setText("");
                    Date date = new Date();
                    MessageModell modell = new MessageModell(msg, senderID, date.getTime());

                    dBase.getReference().child("chats").child(senderRoom).child("messages").push().setValue(modell).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dBase.getReference().child("chats").child(partnerRoom).child("messages").push().setValue(modell).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatView.super.onBackPressed();
            }
        });
    }


    private void hideKeyBoard(View view) {


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void scrollToBottom() {
        msgAdapter.scrollToPosition(messageList.size() - 1);
    }



}

