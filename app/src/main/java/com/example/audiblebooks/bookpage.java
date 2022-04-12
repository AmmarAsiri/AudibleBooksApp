package com.example.audiblebooks;

import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class bookpage extends AppCompatActivity {
    private static final String TAG = "bookpage";
    private EditText txt;
    private Button speakbtn, stopbtn;
    private TextToSpeech tts;
    private DatabaseReference myRef;
    private FirebaseDatabase firebaseDatabase;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookpage);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("message");

        txt = findViewById(R.id.txt);
        speakbtn = findViewById(R.id.speakbtn);
        stopbtn = findViewById(R.id.stopbtn);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                txt.setText(value);
                Toast.makeText(bookpage.this, "Updated", Toast.LENGTH_SHORT).show();
                String url = txt.getText().toString();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookpage.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                } else {
                    Toast.makeText(bookpage.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = txt.getText().toString().trim();
                if (toString().equals("")) {
                    Toast.makeText(bookpage.this, "please Enter Text...!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(bookpage.this, "SPEAKING..", Toast.LENGTH_SHORT).show();
                    myRef.push().setValue(txt.getText().toString());
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tts.isSpeaking()) {
                    tts.stop();
                    tts.shutdown();
                } else {
                    Toast.makeText(bookpage.this, "Not Speaking..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if (tts != null || tts.isSpeaking()) {
            tts.stop();
            //tts.shutdown();
            super.onPause();
        }
    }
}

