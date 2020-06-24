package com.sschuraytz.charcoaldrawing;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar drawingThickness;
    private DrawingView drawingView;
    private ImageButton drawButton;
    private ImageButton eraseButton;
    private ImageButton undoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawingView();
        setUpSlider();
        setUpDraw();
        setUpErase();
        setUpUndo();
    }

    public void setUpDrawingView()
    {
        drawingView = (DrawingView) findViewById(R.id.canvas);
        drawingView.setOnTouchListener((v, event) -> {
            updateUndoVisibility();
            return false;
        });
    }

    public void setUpSlider()
    {
        drawingThickness = (SeekBar) findViewById(R.id.thicknessSlider);
        drawingView.setRadius(drawingThickness.getProgress());
        drawingThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.setRadius(drawingThickness.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void hideSystemUI()
    {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN  //hide status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void setUpDraw()
    {
        drawButton = (ImageButton) findViewById(R.id.drawButton);
        drawButton.setOnClickListener(v -> drawingView.setDrawingMode());
    }
    public void setUpErase()
    {
        eraseButton = (ImageButton) findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener( v -> drawingView.setEraseMode());
    }

    public void setUpUndo()
    {
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(v -> {
            drawingView.undo();
            updateUndoVisibility();
        });
    }

    public void updateUndoVisibility()
    {
        if (drawingView.undoRedo.getCurrentStackSize() > 1) {
            undoButton.setVisibility(View.VISIBLE);
        }
        else {
            undoButton.setVisibility(View.GONE);
        }
    }
}
