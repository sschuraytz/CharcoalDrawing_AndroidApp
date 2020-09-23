package com.sschuraytz.charcoaldrawing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Console;
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
        View alertDialogView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.save_dialog_fragment, null);
        alertDialogBuilder.setView(alertDialogView);
        EditText drawingTitle = alertDialogView.findViewById(R.id.drawing_name);
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
             if (drawingTitle.getText().length() > 0) {
                 saveDrawing.saveBitmap(activity, bitmapToSave, drawingTitle.getText().toString());
             } else {
                 saveDrawing.saveBitmap(activity, bitmapToSave);
             }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}