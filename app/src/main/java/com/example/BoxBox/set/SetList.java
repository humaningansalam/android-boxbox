package com.example.BoxBox.set;


import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class SetList {


    public ArrayList<String> getlist(File directory) {
        ArrayList<String> list = new ArrayList<>();
        if (directory == null || !directory.exists()) {
            Log.e("SetList", "Directory is null or does not exist: " + (directory != null ? directory.getPath() : "null"));
            return list;
        }
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".mp4");
            }
        });
        if (files != null && files.length > 0) {
            for (File file : files) {
                list.add(file.getName());
                Log.d("SetList", "Added file: " + file.getName());
            }
        } else {
            Log.e("SetList", "No .mp4 files found in directory: " + directory.getPath());
        }
        return list;
    }

}
