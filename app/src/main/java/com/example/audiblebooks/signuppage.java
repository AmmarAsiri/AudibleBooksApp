package com.example.audiblebooks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class signuppage extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText username;
    EditText password;
    EditText email;
    Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        TextView btn = findViewById(R.id.alreadyHaveAccount);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signupbtn = findViewById(R.id.signupbtn);
        fAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signuppage.this,MainActivity.class));
            }
        });

        signupbtn.setOnClickListener(view -> {
            createUser();
        });
    }

    private void createUser(){
        String user = username.getText().toString();
        String Email = email.getText().toString();
        String Pass = password.getText().toString();

        if (TextUtils.isEmpty(Email)) {
            email.setError("Email cannot be empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(Pass)) {
            password.setError("Password cannot be empty");
            password.requestFocus();
        } else if (TextUtils.isEmpty(Pass)) {
            username.setError("Username cannot be empty");
            username.requestFocus();
        } else {
            fAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(signuppage.this, "user created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signuppage.this,MainActivity.class));
                    } else {
                        Toast.makeText(signuppage.this, "user creation FAILED!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}