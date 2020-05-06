package com.sschuraytz.charcoaldrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawingView drawingView = new DrawingView(this);
        setContentView(drawingView);
    }
}
