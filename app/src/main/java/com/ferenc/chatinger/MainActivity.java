package com.ferenc.chatinger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fauth = FirebaseAuth.getInstance();

        if(fauth.getCurrentUser() == null){
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
        }

    }
}