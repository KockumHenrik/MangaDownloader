package com.example.KissMangaDAO;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KissMangaImageGetter {
    private String baseUrl = "https://kissmanga.org";

    public List<String> getImageUrlList(String targetUrl) throws IOException {
        KissMangaPageSourceApi kissMangaPageSourceApi = new KissMangaPageSourceApi();
        String pageSource = kissMangaPageSourceApi.fetchPageSource(baseUrl + targetUrl);
        String[] eachLine = pageSource.split("\n");
        List<String> imgSourcesTemp = new ArrayList<>();
        for(String s : eachLine){
            if(s.contains("img src")){
                imgSourcesTemp.add(s);
            }
        }
        imgSourcesTemp.remove(imgSourcesTemp.size()-1);
        imgSourcesTemp.remove(0);
        List<String> imageSources = new ArrayList<>();
        for(String i : imgSourcesTemp){
            String tmp = i.split("<img src=\"")[1].split("\">")[0];
            imageSources.add(tmp);
        }
        return imageSources;
    }

    public List<byte[]> downloadImageList(List<String> imageUrls) throws IOException {
        List<byte[]> responseList = new ArrayList<>();
        for(int i = 0; i < imageUrls.size(); i++) {
            URL url = new URL(imageUrls.get(i));
            try{
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();

//                mangaName = mangaName.trim()
//                        .replace(" ", "-")
//                        .replace("&#39;", "'")
//                        .replace(".", "")
//                        .replace(":", "_")
//                        .replace("?", "")
//                        .replace("!", "");
//                System.out.println(mangaName);
//                File directoryMangaName = new File(String.valueOf(targetDirectory + "/" + mangaName));
//                if (!directoryMangaName.exists()) {
//                    directoryMangaName.mkdir();
//                }
//                chapter = chapter.trim()
//                        .replace(" ", "-")
//                        .replace("&#39;", "'")
//                        .replace(".", "")
//                        .replace(":", "_")
//                        .replace("?", "")
//                        .replace("!", "");
//                System.out.println(chapter);
//                File directoryChapter = new File(String.valueOf(targetDirectory + "/" + mangaName + "/" + chapter));
//                if (!directoryChapter.exists()) {
//                    directoryChapter.mkdir();
//                }

//                FileOutputStream fos = new FileOutputStream(targetDirectory + "/" + mangaName + "/" + chapter + "/Image-"+i+".jpg"); //TODO: Move this saving part to somewhere else, maybe controller
//                fos.write(response);
//                fos.close();
                responseList.add(response);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        return responseList;
    }
}

