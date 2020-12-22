package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SaveDrawing {

    protected Activity baseActivity;
    private Canvas saveCanvas;
    private Bitmap bitmapToSave;

    public SaveDrawing(Activity activity) {
        baseActivity = activity;
    }

    public void addWhiteBackgroundToBitmap(Bitmap inputBitmap) {
        bitmapToSave = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        saveCanvas = new Canvas(bitmapToSave);
        //this prevents saved drawings from displaying black charcoal on default (black) background
        saveCanvas.drawColor(Color.WHITE);
        saveCanvas.drawBitmap(inputBitmap, 0, 0, null);
    }

    public void saveBitmap(Activity activity, Bitmap bitmap) {
        // some apps save drawings as "Untitled". here it's saved as sketch{randomNum}.png
        Random rand = new Random();
        int n = 10000;
        n = rand.nextInt(n);
        String fileName = "sketch" + n;
        saveBitmap(activity, bitmap, fileName);
    }

    //https://stackoverflow.com/a/49024173
    //https://stackoverflow.com/a/56990305
    public void saveBitmap(@NonNull Activity activity, @NonNull Bitmap bitmap, @NonNull final String displayName) {
        OutputStream outStream;
        addWhiteBackgroundToBitmap(bitmap);
        try {
            // TODO: determine how to save image so that it is editable (from the gallery)
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
                bitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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
                bitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, outStream);
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

