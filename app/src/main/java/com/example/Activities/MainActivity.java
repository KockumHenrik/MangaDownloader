package com.example.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.Controller.Controller;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Controller controller;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller();
        activity = this;

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
    }

    public void clickDownloadManga(View view) throws IOException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TextInputEditText txtFieldMangaUrl = findViewById(R.id.txtFieldMangaUrl);
                String url = txtFieldMangaUrl.getText().toString();
                if(!url.isEmpty()){
                    Snackbar snack = Snackbar.make(view, "Started Downloading", BaseTransientBottomBar.LENGTH_LONG);
                    snack.show();
                    try {
                        controller.downloadKissMangaManga(url, activity, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
//        Snackbar snack = Snackbar.make(view, "Started Downloading", BaseTransientBottomBar.LENGTH_LONG);
//        snack.show();
        thread.start();
    }

    public void clickGoToMangaView(View view){
        startActivity(new Intent(MainActivity.this, MangaViewActivity.class));
    }

}