package com.example.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Controller.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangaReadActivity extends AppCompatActivity {
    ScrollView scrollView;
    Controller controller;
    Activity activity;
    String currentManga;
    Bundle extras;
    int position;
    LinearLayout newMangaView;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_read);
        scrollView = findViewById(R.id.scrollViewRead);
        controller = new Controller();
        activity = this;
        newMangaView = findViewById(R.id.layoutMangaRead);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) newMangaView.getLayoutParams();
        newMangaView.setLayoutParams(params);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                extras = getIntent().getExtras();
                currentManga = extras.getString("mangaName");
//                position = Integer.parseInt(extras.getString("position"));
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.scrollTo(0, position);
//                    }
//                });

                ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File mangaPath = new File(directory.getAbsolutePath() + "/" + currentManga);
                File[] chapters = mangaPath.listFiles();

                for(File i : chapters){
                    File[] indexes = i.listFiles();
                    for(File j : indexes) {
                        ImageView imgView = new ImageView(activity);
                        Bitmap b = controller.loadImageFromStorage(j.getAbsolutePath());
                        imgView.setImageBitmap(b);
                        imgView.setBackground(getDrawable(R.drawable.border));
                        imgView.setAdjustViewBounds(true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newMangaView.addView(imgView);
                            }
                        });
                    }
                }
            }
        });
        if(thread.isInterrupted()){
            thread.run();
        }
        thread.start();
    }

    public void clickGoToMangaViewFromRead(View view){
        Intent intent = new Intent(MangaReadActivity.this, MangaViewActivity.class);

//        String pos = "" + scrollView.getScrollY();
//        HashMap<String,String> map = new HashMap<>();
//        map.put("manga", currentManga);
//        map.put("position", pos);
//        intent.putExtra("prevManga", currentManga);
//        intent.putExtra(currentManga, map);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newMangaView.removeAllViews();
                newMangaView.cancelPendingInputEvents();
            }
        });
        thread.interrupt();
        startActivity(intent);
    }
}
