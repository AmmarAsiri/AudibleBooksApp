package com.example.audiblebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivilegedAction;

public class Account extends AppCompatActivity {

    FirebaseAuth fAuth;
    private TextView emailView;
    private TextView usernameView;
    private TextView tvName;
    private ImageView pfpic;
    private static final String USERS = "users";

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        emailView = findViewById(R.id.emailtxt);
        usernameView = findViewById(R.id.usertxt);
        tvName = findViewById(R.id.tv_name);
        pfpic = findViewById(R.id.pfp);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(USERS);
        userID = firebaseUser.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userAcc = snapshot.getValue(Users.class);

                if (userAcc != null) {
                    String Name = userAcc.getName();
                    String Email = userAcc.getEmail();

                    tvName.setText(Name);
                    usernameView.setText(Name);
                    emailView.setText(Email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account.this, "Something wrong happened!!", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bnv = findViewById(R.id.bottomNavigation);

        bnv.setSelectedItemId(R.id.itemAcc);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemUpload:
                        startActivity(new Intent(getApplicationContext(),Upload.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemHome:
                        startActivity(new Intent(getApplicationContext(),homepage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemAcc:
                        return true;
                    case R.id.itemSettings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}