package com.ferenc.chatinger;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Registration extends AppCompatActivity {

    EditText password, password2, userName,userEmail;
    Button btnLogin;
    FirebaseAuth fauth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Intent intent;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        password = findViewById(R.id.txtFieldPassword);
        password2 = findViewById(R.id.txtFieldPassword2);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.txtFieldEmail);
        btnLogin = findViewById(R.id.btnLogin);

        fauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chatinger-d3269-default-rtdb.europe-west1.firebasedatabase.app/");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(userEmail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(password2.getText().toString())){

                    if((password.getText().toString().length() > 7)){

                        if(password.getText().toString().equals(password2.getText().toString())){

                            if(userEmail.getText().toString().matches(emailPattern)){
                                intent = new Intent(Registration.this, Login.class);
                                processRegistration();
                                startActivity(intent);
                                finish();

                            }
                            else{
                                userEmail.setError("!");
                                Toast.makeText(Registration.this,"Ungültige E-mail Adresse!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            password.setError("!");
                            password2.setError("!");
                            Toast.makeText(Registration.this,"Kennwörter stimmen nicht überein!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        password.setError("!");
                        Toast.makeText(Registration.this,"Kennwort muss mindestens 8 Zeichnen lang sein!", Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(Registration.this,"Es müssen alle Felder ausgefüllt sein!", Toast.LENGTH_LONG).show();
                }




            }
        });
    }

    private void processRegistration() {

        String name = userName.getText().toString();
        String userPassword = password.getText().toString();
        String email = userEmail.getText().toString();

        fauth.createUserWithEmailAndPassword(email, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = task.getResult().getUser().getUid();
                    DatabaseReference reference = database.getReference().child("user").child(id);

                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String userToken = task.getResult();
                            User user = new User(name, email, userPassword, id, userToken);
                            // Log and toast

                            Log.d("ÍRD_KI", userToken);


                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "Ein Fehler ist aufgetreten bei der Registration!", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }
                    });
                }

            }
        });
    }
}