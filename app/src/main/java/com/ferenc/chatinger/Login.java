package com.ferenc.chatinger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button btnReg, btnLogin;

    FirebaseAuth fauth;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txtFieldEmailLog);
        password = findViewById(R.id.txtFieldPasswordLog);

        btnLogin = findViewById(R.id.btnLoginLog);
        btnReg = findViewById(R.id.btnRegistrationLog);

        fauth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {

                    if(email.getText().toString().matches(emailPattern)){

                        if((password.getText().toString().length() > 7)){

                            String userEmail = email.getText().toString();
                            String userPassword = password.getText().toString();

                            fauth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(Login.this,"Unbekannte E-mail Adresse oder Kennwort ist falsch!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else {
                            password.setError("!");
                            Toast.makeText(Login.this,"Kennwort muss mindestens 8 Zeichnen lang sein!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        email.setError("!");
                        Toast.makeText(Login.this,"Ungültige E-mail Adresse!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Login.this,"Es müssen alle Felder ausgefüllt sein!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}