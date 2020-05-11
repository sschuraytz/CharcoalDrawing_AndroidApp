package com.sschuraytz.charcoaldrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar drawingThickness;
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView) findViewById(R.id.canvas);
        setUpSlider();
    }

    public void setUpSlider()
    {
        drawingThickness = (SeekBar) findViewById(R.id.thicknessSlider);
        drawingView.getPaint().setStrokeWidth(drawingThickness.getProgress());
        drawingThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.getPaint().setStrokeWidth(drawingThickness.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


}
