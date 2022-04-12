package com.example.audiblebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class ClassicBook extends AppCompatActivity {

    private TextToSpeech tts;
    private TextView content;
    private Button read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_book);

        content = findViewById(R.id.textContent);
        read = findViewById(R.id.readbtn);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("");

                String stringParser;
                try {
                    PdfReader pdfReader = new PdfReader(file.getPath());
                    stringParser = PdfTextExtractor.getTextFromPage(pdfReader,1).trim();
                    pdfReader.close();
                    content.setText(stringParser);
                    tts.speak(stringParser,TextToSpeech.QUEUE_FLUSH,null,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void readBtnOnClick(View view) {
    }
}