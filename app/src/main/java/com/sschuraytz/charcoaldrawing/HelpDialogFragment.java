package com.sschuraytz.charcoaldrawing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class HelpDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("HELP");
        alertDialogBuilder.setView(R.layout.help_dialog_fragment);
        // setMessage pushes buttons of the screen --> dialog subtitle has been moved to xml view
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        return alertDialogBuilder.create();
    }
}
