package com.example.audiblebooks;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class uploadpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadpage);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case (R.id.menuHome):
                            setContentView(R.layout.homepage);
                            break;
                        case (R.id.menuUpload):
                            setContentView(R.layout.uploadpage);
                            break;
                        case (R.id.menuAcc):
                            setContentView(R.layout.accountpage);
                            break;
                        case (R.id.menuSettings):
                            setContentView(R.layout.settingspage);
                            break;
                    }
                    return true;
                }


            };
}







