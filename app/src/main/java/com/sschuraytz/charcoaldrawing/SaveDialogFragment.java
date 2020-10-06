package com.sschuraytz.charcoaldrawing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {

    private SaveDrawing saveDrawing;

    // avoid passing args through constructor because when Android
    // recreates the fragment, the dialog is recreated with default constructor
    // thus, args are passed via a bundle created in saveDrawing() in MainActivity


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveDrawing = new SaveDrawing(getActivity());
    }

    // check permissions here, b/c saveDrawing does not exist until after onCreate()
    @Override
    public void onStart() {
        super.onStart();
        saveDrawing.checkExternalStoragePermissions();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        Bitmap bitmapToSave = getArguments().getParcelable("bitmap");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        //alertDialogBuilder.setIcon(R.drawable.ic_save_foreground);
        alertDialogBuilder.setTitle("SAVE AS:");
        View alertDialogView = Objects.requireNonNull(activity.getLayoutInflater().inflate(R.layout.save_dialog_fragment, null));
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