package com.sschuraytz.charcoaldrawing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("SAVE");
        alertDialogBuilder.setView(R.layout.save_dialog_fragment);
        //drawing title
        //artist name
        //drawing description
        alertDialogBuilder.setMessage("Save As:");
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}