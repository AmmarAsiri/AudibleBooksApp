package com.example.audiblebooks;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class bookpage extends AppCompatActivity {
    EditText txt;
    Button speakbtn, stopbtn;
    TextToSpeech tts;
    private DatabaseReference myRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookpage);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        txt = findViewById(R.id.txt);
        speakbtn = findViewById(R.id.speakbtn);
        stopbtn = findViewById(R.id.stopbtn);

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
