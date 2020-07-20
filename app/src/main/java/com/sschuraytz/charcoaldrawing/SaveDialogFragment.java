package com.sschuraytz.charcoaldrawing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {

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
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            //could allow user to choose jpeg or png
            //saveDrawing.saveBitmap(getApplicationContext(), drawingView.undoRedo.getCurrentBitmap(), Bitmap.CompressFormat.JPEG,"image/jpeg", "user input");
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}