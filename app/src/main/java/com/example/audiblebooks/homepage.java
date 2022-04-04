package com.example.audiblebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.QuickContactBadge;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepage extends AppCompatActivity {

    private ImageView searchBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        searchBooks = findViewById(R.id.searchBooks);

        searchBooks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
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

