package com.ferenc.chatinger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fauth;
    RecyclerView userList;
    UserAdapter uAdapter;
    FirebaseDatabase dBase;

    ArrayList<User> users;

    ImageButton btnStartChat, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        users = new ArrayList<User>();
        dBase = FirebaseDatabase.getInstance("https://chatinger-d3269-default-rtdb.europe-west1.firebasedatabase.app/");
        fauth = FirebaseAuth.getInstance();
        uAdapter = new UserAdapter(MainActivity.this, users);

        DatabaseReference reference = dBase.getReference().child("user");


        userList = findViewById(R.id.mainUserList);
        userList.setLayoutManager(new LinearLayoutManager(this));
        userList.setAdapter(uAdapter);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {

                    System.out.println(item.getValue(User.class).toString());

                    User user = item.getValue(User.class);

                    users.add(user);

                }
                uAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog);

                Button yes, no;
                yes = dialog.findViewById(R.id.btnYes);
                no = dialog.findViewById(R.id.btnNo);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });



        if(fauth.getCurrentUser() == null){
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
        }

    }
}