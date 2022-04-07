package com.example.audiblebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText user;
    EditText pass;
    Button login;

    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btn = findViewById(R.id.signuptxt);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.loginbtn);

        fAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,signuppage.class));
            }
        });

        login.setOnClickListener(view -> {
            loginMethod();
        });

        }

        private void loginMethod() {
            String Email = user.getText().toString();
            String Pass = pass.getText().toString();

            if (TextUtils.isEmpty(Email)) {
                user.setError("Email cannot be empty");
                user.requestFocus();
            } else if (TextUtils.isEmpty(Pass)) {
                pass.setError("Password cannot be empty");
                pass.requestFocus();
            } else {
                fAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,homepage.class));
                        } else {
                            Toast.makeText(MainActivity.this, "login FAILED!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }
