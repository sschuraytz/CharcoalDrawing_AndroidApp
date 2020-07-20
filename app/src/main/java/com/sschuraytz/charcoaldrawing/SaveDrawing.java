package com.sschuraytz.charcoaldrawing;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.NonNull;


public class SaveDrawing {

    protected Context baseContext;

    public SaveDrawing(Context context) {
        baseContext = context;
    }

    private void setUp() {
        //media-database-columns-to-retrieve
        //could be set to null if want to return ALL columns
        String[] projection = new String[] {
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };

        //sql-where-clause-with-placeholder-variables;
        //could be set to null if want to return ALL rows
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE+"=?";

        //values-of-placeholder-variables
        String[] selectionArgs = {"MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE"};

        //sql-order-by-clause
        String sortOrder = MediaStore.Images.Media.TITLE + " ASC";

        Cursor imageCursor = baseContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            );

        if (imageCursor != null) {
            if (imageCursor.moveToFirst()) {
/*                do {

                }*/
            }
        }
        while (imageCursor.moveToNext()) {
            //projection.;
        }

        imageCursor.close();
    }

    //https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore
    public void saveBitmap(@NonNull Context context, @NonNull Bitmap bitmap,
                           @NonNull Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName) {
        OutputStream outStream;
        String relativeLocation = Environment.DIRECTORY_PICTURES;

        try {
            //TODO: add this to app requirements?
            if (Build.VERSION.SDK_INT >= 29) {


                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                //perhaps shouldn't use this because of filesystem permissions
                contentValues.put(MediaStore.Images.Media.DATA, relativeLocation);

                ContentResolver contentResolver = context.getContentResolver();
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uri = contentResolver.insert(contentUri, contentValues);


                outStream = contentResolver.openOutputStream(uri);
            } else {
                String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File image = new File(imagePath, displayName);
                outStream = new FileOutputStream(image);
            }

            try {
                bitmap.compress(format, 100, outStream);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (outStream != null) {
                    outStream.close();
                }
            }
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }
}

