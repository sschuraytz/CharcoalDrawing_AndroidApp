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
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SaveDrawing {

    protected Context baseContext;
    protected Activity baseActivity;

    public SaveDrawing(Context context, Activity activity) {
        baseContext = context;
        baseActivity = activity;
    }

    //https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore
    public void saveBitmap(@NonNull Context context, @NonNull Bitmap bitmap,
                           @NonNull Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName) {
        OutputStream outStream;

        try {
            //TODO: decide if want alternate implementations based on version
            //TODO: determine how to save image so it displays when click on thumbnail
            //TODO: determine how to save image so that it is editable (from the gallery)
                        //and open-able from "images" in settings the same way it's accessible from "shared" in settings
            //TODO: add this to app requirements?
            if (Build.VERSION.SDK_INT >= 29) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                //perhaps shouldn't use this because of filesystem permissions
                //contentValues.put(MediaStore.Images.Media.DATA, relativeLocation);

                ContentResolver contentResolver = context.getContentResolver();
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uri = contentResolver.insert(contentUri, contentValues);


                outStream = contentResolver.openOutputStream(uri);
                bitmap.compress(format, 100, outStream);
                if (outStream != null) {
                    outStream.close();
                }
            } else {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Charcoal Drawings";
                File outputDir = new File(path);
                outputDir.mkdirs(); //creates if doesn't exist
                File newImageFile = new File(path + File.separator + displayName + ".png");
                FileOutputStream outputStream = new FileOutputStream(newImageFile);
                bitmap.compress(format, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                //Android only runs full media scan on reboot, to add drawing to media library immediately:
                //pass newly created image file to media scanner service to be added to the phone gallery
                MediaScannerConnection.scanFile(baseContext, new String[]{newImageFile.toString()},
                        new String[]{newImageFile.getName()},
                        (path1, uri) -> Toast.makeText(baseContext, newImageFile.getName() + "was saved to gallery", Toast.LENGTH_SHORT).show());
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


/*
        long id = ContentUris.parseId(url);
        Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        storeThumbnail(contentResolver, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
 */

/*    private Bitmap storeThumbnail(
            ContentResolver contentResolver, Bitmap bitmap, long id,
            float width, float height, int kind) {
        Matrix matrix = new Matrix();

        float scaleX = width / bitmap.getWidth();
        float scaleY = height / bitmap.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id);
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());

        Uri url = contentResolver.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = contentResolver.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }

    }*/

}

