package com.example.mycampuscompanion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MediaUtils {

    private static final String TAG = "MediaUtils";

    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "Fichier image créé : " + image.getAbsolutePath());
        return image;
    }

    public static File createVideoFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);

        File video = File.createTempFile(videoFileName, ".mp4", storageDir);
        Log.d(TAG, "Fichier vidéo créé : " + video.getAbsolutePath());
        return video;
    }

    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(
                context,
                context.getApplicationContext().getPackageName() + ".fileprovider",
                file
        );
    }

    public static double getFileSizeMB(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return file.length() / (1024.0 * 1024.0);
    }
}