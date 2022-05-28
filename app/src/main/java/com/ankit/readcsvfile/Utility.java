package com.ankit.readcsvfile;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utility {
    public static void downloadSampleCSV(Context context, String path, String outPath) {
        AssetManager assetManager = context.getAssets();
        String assets[];
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(context, path, outPath);
            } else {
                String fullPath = outPath + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    if (!dir.mkdir()) Log.e("4356", "No create external directory: " + dir);
                for (String asset : assets) {
                    downloadSampleCSV(context, path + "/" + asset, outPath);
                }
            }
        } catch (IOException ex) {
            Log.e("123", "I/O Exception", ex);
        }
    }

    private static void copyFile(Context context, String filename, String outPath) {
        AssetManager assetManager = context.getAssets();

        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            String newFileName = outPath + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("678", e.getMessage());
        }
    }
}
