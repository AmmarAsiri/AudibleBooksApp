package com.example.audiblebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button login;
    private TextView signup;
    private TextView passReset;

    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.signuptxt);
        passReset = findViewById(R.id.forgotpass);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.loginbtn);

        fAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,signuppage.class));
            }
        });

        login.setOnClickListener(view -> {
            loginMethod();
        });

        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        }

        private void loginMethod() {
            String Email = email.getText().toString();
            String Pass = pass.getText().toString();

            if (TextUtils.isEmpty(Email)) {
                email.setError("Email cannot be empty");
                email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                email.setError("Please Enter Valid Email!!");
                email.requestFocus();
            } else if (TextUtils.isEmpty(Pass)) {
                pass.setError("Password cannot be empty");
                pass.requestFocus();
            } else if (Pass.length() < 6) {
                pass.setError("Min password length should be 6 characters!!");
                pass.requestFocus();
            } else {
                fAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,homepage.class));
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your Email to verify your account!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "login FAILED!! Check Email and Password!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }