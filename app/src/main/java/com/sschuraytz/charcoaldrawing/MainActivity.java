package com.sschuraytz.charcoaldrawing;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View decorView;
    private int uiOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideTopBar();
    }

    public void setSystemUI()
    {
        decorView = getWindow().getDecorView();
        uiOptions =
                  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN  //hide status bar
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        hideTopBar();
    }

    public void hideTopBar()
    {
        decorView.setSystemUiVisibility(uiOptions);
    }
}
