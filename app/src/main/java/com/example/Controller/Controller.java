package com.example.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.view.View;
import android.widget.Toast;

import com.example.KissMangaDAO.KissMangaImageGetter;
import com.example.KissMangaDAO.KissMangaPageSourceApi;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Controller {
//    String directory = "D:\\Projekt\\MangaDownloader\\Pictures";

    public void downloadKissMangaManga(String targetUrl, Activity activity, View view) throws IOException {
        KissMangaPageSourceApi kissMangaPageSourceApi = new KissMangaPageSourceApi();
        KissMangaImageGetter kissMangaImageGetter = new KissMangaImageGetter();
        List<String> chapterList = kissMangaPageSourceApi.getChapterList(targetUrl);
        Map<String, String> map = kissMangaPageSourceApi.getChapterMap(targetUrl);

        Collections.reverse(chapterList);

        for(String c : chapterList) {
            List<String> imageSources = kissMangaImageGetter.getImageUrlList(map.get(c.split(" - ")[1]));            
            List<byte[]> images = kissMangaImageGetter.downloadImageList(imageSources);

            int chapterIndex = 0;
            for (byte[] b : images) {
                String imageName = c;
                saveToInternalStorage(BitmapFactory.decodeByteArray(b, 0, b.length), activity, imageName, chapterIndex);
                chapterIndex++;
                System.out.println("-Downloaded Chapter/image: " + imageName.trim() + "/" + chapterIndex);
            }
        }


    }

    private String saveToInternalStorage(Bitmap bitmapImage, Activity activity, String imageName, int index){
        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        String mangaName = imageName.split(" - ")[0].trim();
        String chapterName = imageName.split(" - ")[1].trim();
        File mangaPath = new File(directory.getAbsolutePath() + "/" + mangaName);
        if(!mangaPath.exists()){
            mangaPath.mkdir();
        }
        File chapterPath = new File(mangaPath.getAbsolutePath() + "/" + chapterName);
        if(!chapterPath.exists()){
            chapterPath.mkdir();
        }
        File myPath=new File(chapterPath, index + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path){
        Bitmap b = null;
        try {
            File f=new File(path);
            FileInputStream fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis);
            fis.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}

