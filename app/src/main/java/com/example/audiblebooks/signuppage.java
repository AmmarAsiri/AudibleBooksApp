package com.example.audiblebooks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class signuppage extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference reference;
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
        reference = FirebaseDatabase.getInstance().getReference("users");

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
        String User = username.getText().toString();
        String Email = email.getText().toString();
        String Pass = password.getText().toString();

        if (TextUtils.isEmpty(Email)) {
            email.setError("Email cannot be empty!!");
            email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please Enter Valid Email!!");
            email.requestFocus();
        } else if (TextUtils.isEmpty(Pass)) {
            password.setError("Password cannot be empty!!");
            password.requestFocus();
        } else if (Pass.length() < 6) {
            password.setError("Min password length should be 6 characters!!");
            password.requestFocus();
        } else if (TextUtils.isEmpty(User)) {
            username.setError("Username cannot be empty!!");
            username.requestFocus();
        } else {
            fAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Users user = new Users(User, Pass, Email);
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(signuppage.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(signuppage.this,MainActivity.class));
                                } else {
                                    Toast.makeText(signuppage.this, "Register Failed!! Try Again..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(signuppage.this, "Register Failed!! Try Again..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}