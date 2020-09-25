package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.google.android.material.snackbar.Snackbar;


public class SaveDrawing {

    protected Activity baseActivity;

    public SaveDrawing(Activity activity) {
        baseActivity = activity;
    }

    public void saveBitmap(Activity activity, Bitmap bitmap) {
        // some apps save drawings as "Untitled"
        Random rand = new Random();
        int n = 10000;
        n = rand.nextInt(n);
        String fileName = "sketch" + n;
        saveBitmap(activity, bitmap, fileName);
    }

    //https://stackoverflow.com/questions/36624756/how-to-save-bitmap-to-android-gallery
    //https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore
    public void saveBitmap(@NonNull Activity activity, @NonNull Bitmap bitmap, @NonNull final String displayName) {
        OutputStream outStream;

        try {
            //TODO: determine how to save image so that it is editable (from the gallery)
                //and open-able from Settings --> Images the same way it's accessible from
                // Settings --> Storage --> Other --> Pictures --> CharcoalDrawings
                // (from there, I can open with a gallery or a few other drawing apps)
                //also, which images are getting saved to which location?
            // need separate implementation based on SDK since getExternalStoragePublicDirectory() was deprecated in SDK Q (29)
            if (Build.VERSION.SDK_INT < 29) {
                File outputDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CharcoalDrawings");
                outputDirectory.mkdirs(); //creates if doesn't exist
                String fileName = displayName + ".png";
                File newImageFile = new File(outputDirectory, fileName);
                if (newImageFile.exists()) {
                    newImageFile.delete();
                }
                FileOutputStream outputStream = new FileOutputStream(newImageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                //Android only runs full media scan on reboot, to add drawing to media library immediately:
                //pass newly created image file to media scanner service to be added to the phone gallery
                MediaScannerConnection.scanFile(baseActivity, new String[]{newImageFile.toString()},
                        new String[]{newImageFile.getName()},
                        (path1, uri) -> confirmSave(newImageFile.getName(), "Media Gallery"));
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                //perhaps shouldn't use this because of filesystem permissions
                //contentValues.put(MediaStore.Images.Media.DATA, relativeLocation);

                ContentResolver contentResolver = activity.getContentResolver();
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uri = contentResolver.insert(contentUri, contentValues);

                outStream = contentResolver.openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                if (outStream != null) {
                    outStream.close();
                }
                confirmSave(displayName + ".png", "Google Photos");
            }
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }

    public void checkExternalStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                baseActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    baseActivity,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    1
            );

        }
    }

    public void confirmSave(String fileName, String fileLocation) {
        Snackbar.make(this.baseActivity.findViewById(R.id.main_layout), fileName + " was saved to " +  fileLocation, Snackbar.LENGTH_LONG).show();
    }
}

