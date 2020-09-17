package com.sschuraytz.charcoaldrawing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class SaveDialogFragment extends DialogFragment {

    SaveDrawing saveDrawing;
    Activity activity;

    // avoid passing args through constructor because when Android
    // recreates the fragment, the dialog is recreated with default constructor
    // thus, args are passed via a bundle created in saveDrawing() in MainActivity

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bitmap bitmapToSave = getArguments().getParcelable("bitmap");
        activity = saveDrawing.baseActivity;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        //alertDialogBuilder.setIcon(R.drawable.ic_save_foreground);
        alertDialogBuilder.setTitle("SAVE AS:");
        alertDialogBuilder.setView(R.layout.save_dialog_fragment);
        //EditText drawingTitle = Objects.requireNonNull(getView()).findViewById(R.id.drawing_name);
        //drawing title
        //artist name
        //drawing description
        //TODO: figure out how to access user EditText input so it can be used as image displayName
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            // if (drawingTitle != null)
            saveDrawing.saveBitmap(activity, bitmapToSave);
           // else { saveDrawing.saveBitmap(baseActivity, bitmapToSave, drawingTitle.toString()); }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}