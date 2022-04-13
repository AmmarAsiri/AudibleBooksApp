package com.example.audiblebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class ScienceBook extends AppCompatActivity {

    private TextToSpeech tts;
    private Button read;
    private TextView text1;
    private PDFView pdfView;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference("urll");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_book);
        pdfView=  findViewById(R.id.pdfView);
        text1= findViewById(R.id.text1);
        read = findViewById(R.id.readbtn);


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                } else {
                    Toast.makeText(ScienceBook.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value=snapshot.getValue(String.class);
                        text1.setText(value);
                        Toast.makeText(ScienceBook.this, "Update", Toast.LENGTH_SHORT).show();
                        String url=text1.getText().toString();
                        new RetrivePdfStream().execute(url);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ScienceBook.this, "Failed to load", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    class RetrivePdfStream extends AsyncTask<String,Void,InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try{
                URL url=new URL (strings[0]);
                HttpsURLConnection urlConnection=(HttpsURLConnection)url.openConnection();
                if (urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());

                }
            }catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

}