package com.example.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MangaViewActivity extends AppCompatActivity {
//    Bundle extras;
    List<Map<String,String>> scrollPositions = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_view);
        LinearLayout newMangaView = findViewById(R.id.layoutMangaView);
//        Bundle extra = getIntent().getExtras();
//        scrollPositions = new ArrayList<>();
//        if(extra != null){
//            scrollPositions.add((Map<String, String>) extra.getSerializable(extra.getString("prevManga")));
//        }
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        String[] arr= directory.list();
        File[] fileArr = directory.listFiles();
        for(int i = 0; i < arr.length; i++) {
            TextView view = new TextView(this);
            view.setText(arr[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MangaViewActivity.this, MangaReadActivity.class);
                    i.putExtra("mangaName", view.getText());
//                    System.out.println("-Scroll list Size: " + scrollPositions.size());
//                    boolean hasBeen = false;
//                    for (Map<String, String> m : scrollPositions) {
//                        if (haveScrollPos(m, view)) {
//                            hasBeen = true;
//                            i.putExtra("position", m.get("position"));
//                        }
//                    }
//                    if(!hasBeen){
//                        i.putExtra("position", "0");
//                    }

                    startActivity(i);
                }
            });
            newMangaView.addView(view);
        }


    }

    public void clickGoToNewMangaView(View view){
        startActivity(new Intent(MangaViewActivity.this, MainActivity.class));
    }

//    private boolean haveScrollPos(Map<String,String> map, TextView view){
//        if(map.get("manga")!=view.getText()){
//            return true;
//        }
//        return false;
//    }
}
