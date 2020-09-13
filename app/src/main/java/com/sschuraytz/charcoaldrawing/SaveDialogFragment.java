package com.sschuraytz.charcoaldrawing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {

    SaveDrawing saveDrawing;
    Activity baseActivity;
    Bitmap bitmapToSave;

    SaveDialogFragment(Activity activity, Bitmap currentBitmap) {
        saveDrawing = new SaveDrawing(activity);
        baseActivity = activity;
        bitmapToSave = currentBitmap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        //alertDialogBuilder.setIcon(R.drawable.ic_save_foreground);
        alertDialogBuilder.setTitle("SAVE AS:");
        alertDialogBuilder.setView(R.layout.save_dialog_fragment);
        //drawing title
        //artist name
        //drawing description
        //TODO: figure out how to access user EditText input so it can be used as image displayName
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            saveDrawing.saveBitmap(baseActivity, bitmapToSave, Bitmap.CompressFormat.PNG,"image/png", "two");
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}