package com.example.KissMangaDAO;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KissMangaPageSourceApi{
    public String fetchPageSource(String targetUrl) throws IOException {
        BufferedReader br = null;
        String downloadURL = URLEncoder.encode(targetUrl, "UTF-8");//Some URL
        URL url = new URL(targetUrl);
        String pageSource = new String();

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                pageSource += line + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return pageSource;
    }

    private String getMangaOverviewSource(String targetUrl) throws IOException {
        String fullSource = fetchPageSource(targetUrl);
        String mangeOverviewSource = fullSource.split("<div class=\"head\">\n" +
                "                  <div>Chapter name</div>\n" +
                "                  <div>Day Added</div>\n" +
                "                </div>")[1].split("<div class=\"bigBarContainer full\">\n" +
                "          <div class=\"barTitle full\">\n" +
                "            <h2>Popular Manga on Kissmanga</h2>\n" +
                "          </div>")[0];
        return mangeOverviewSource;
    }

    public List<String> getChapterList(String targetUrl) throws IOException {
        String chapterSource = getMangaOverviewSource(targetUrl);
        String[] eachChapterSource = chapterSource.split("\n" +
                "                \n" +
                "                ");
        List<String> chapters = new ArrayList<>();
        for(int i = 1; i  < eachChapterSource.length; i++){
            String c = eachChapterSource[i].split("<a href=")[1]
                    .split(">")[1]
                    .split("</a>")[0]
                    .replace("</a", "")
                    .replace("\n", "")
                    .replaceAll("\\s+", " ");
            chapters.add(c);
        }
        return chapters;
    }

    public List<String> getUrlList(String targetUrl) throws IOException{
        String chapterSource = getMangaOverviewSource(targetUrl);
        String[] eachChapterSource = chapterSource.split("\n" +
                "                \n" +
                "                ");
        List<String> urls = new ArrayList<>();
        for(int i = 1; i  < eachChapterSource.length; i++){
            String u = eachChapterSource[i].split("<a href=\"")[1]
                    .split("\"\n" +
                            "                        title=")[0];
            urls.add(u);
        }
        return urls;
    }

    public Map<String,String> getChapterMap(String targetUrl) throws  IOException{
        Map<String, String> chapterMap = new HashMap<>();
        List<String> chapters = getChapterList(targetUrl);
        List<String> urls = getUrlList(targetUrl);
        for(int i = 0; i < chapters.size(); i++){
            chapterMap.put(chapters.get(i).split(" - ")[1], urls.get(i));
        }
        return chapterMap;
    }




}

