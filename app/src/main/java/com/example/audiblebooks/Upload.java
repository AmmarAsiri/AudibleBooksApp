package com.example.audiblebooks;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class Upload extends AppCompatActivity {

    private TextToSpeech tts;

    Button browsebtn;
    Button play;
    Button pause;
    private TextView content;
    private EditText pdfName;

    FirebaseAuth fAuth;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        fAuth = FirebaseAuth.getInstance();

        browsebtn = findViewById(R.id.browse);
        play = findViewById(R.id.playbtn);
        pause = findViewById(R.id.pausebtn);
        content = findViewById(R.id.fileContent);
        content.setMovementMethod(new ScrollingMovementMethod());
        pdfName = findViewById(R.id.pdfName);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                } else {
                    Toast.makeText(Upload.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // getting user permission for external storage
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        BottomNavigationView bnv = findViewById(R.id.bottomNavigation);
        bnv.setSelectedItemId(R.id.itemUpload);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemUpload:
                        return true;
                    case R.id.itemHome:
                        startActivity(new Intent(getApplicationContext(), homepage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.itemAcc:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.itemSettings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callChooseFileFromDevice();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = content.getText().toString().trim();
                if (toString().equals("")) {
                    Toast.makeText(Upload.this, "NO Text...!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Upload.this, "SPEAKING..", Toast.LENGTH_SHORT).show();
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tts.isSpeaking()) {
                    tts.stop();
                    tts.shutdown();
                } else {
                    Toast.makeText(Upload.this, "Not Speaking!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void callChooseFileFromDevice() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf..."), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && resultData != null && resultData.getData() != null) {

            UploadFiles(resultData.getData());

        }
    }


    private void UploadFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri url = uriTask.getResult();

                        pdfClass pdfClass = new pdfClass(pdfName.getText().toString(), url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);

                        Toast.makeText(Upload.this, "file uploaded", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded:" + (int) progress + "%");

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