package com.ferenc.chatinger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.util.Date;

public class ChatView extends AppCompatActivity {

    String partnerID, partnerName, senderID, senderRoom, partnerRoom;
    TextView uName;
    CardView btnSend;
    EditText message;

    FirebaseAuth fauth;
    FirebaseDatabase dBase;

    RecyclerView msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        fauth = FirebaseAuth.getInstance();
        dBase = FirebaseDatabase.getInstance("https://chatinger-d3269-default-rtdb.europe-west1.firebasedatabase.app/");

        partnerName = getIntent().getStringExtra("userName");
        partnerID = getIntent().getStringExtra("userID");
        senderID = fauth.getUid();

        btnSend = findViewById(R.id.btnSendChV);
        message = findViewById(R.id.txtMessage);
        msgAdapter = findViewById(R.id.msgAdapter);

        uName = findViewById(R.id.userNameChv);
        uName.setText(partnerName);

        DatabaseReference chatReference = dBase.getReference().child("user").child(senderRoom).child("messages");

        senderRoom = senderID+partnerID;
        partnerRoom = partnerID+senderID;

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(message.getText().toString())){

                    String msg = message.getText().toString();
                    message.setText("");
                    Date date = new Date();
                    MessageModell modell = new MessageModell(msg, senderID, date.getTime());

                    dBase.getReference().child("chats").child("senderRoom").child("messages").push().setValue(modell).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dBase.getReference().child("chats").child("partnerRoom").child("messages").push().setValue(modell).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                }
                else{
                    Toast.makeText(ChatView.this, "Schreibe zuerst eine Nachricht!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}