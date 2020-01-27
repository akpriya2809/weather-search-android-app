package com.example.weather_app;


import java.util.ArrayList;
import java.util.List;


public class PictureContent {


    public static final List<PictureItem> ITEMS = new ArrayList<>();

    public static void loadImage(String src) {

        //Uri uri = Uri.fromFile(file);
        PictureItem newItem = new PictureItem(src);
        addItem(newItem);
    }

    private static void addItem(PictureItem item) {
        ITEMS.add(item);
    }

    public static void loadSavedImages(String[] imageUrls) {
        ITEMS.clear();
        for(int i =0; i<imageUrls.length;i++){
            loadImage(imageUrls[i]);
        }


    }


    public static class PictureItem {
        public final String src;

        public PictureItem(String src) {
            this.src = src;
        }

    }
}
