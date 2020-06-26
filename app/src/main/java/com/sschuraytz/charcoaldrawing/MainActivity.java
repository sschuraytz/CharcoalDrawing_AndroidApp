package com.sschuraytz.charcoaldrawing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements UndoRedoListener {

    private SeekBar drawingThickness;
    private DrawingView drawingView;
    private ImageButton undoButton;
    private ImageButton redoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawingView();
        setUpSlider();
        setUpDraw();
        setUpErase();
        setUpSmudge();
        setUpUndo();
        setUpRedo();
    }

    public void setUpDrawingView()
    {
        drawingView = (DrawingView) findViewById(R.id.canvas);
        drawingView.undoRedo.setListener(this);
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
        ImageButton drawButton = (ImageButton) findViewById(R.id.drawButton);
        drawButton.setOnClickListener(v -> drawingView.setDrawingMode());
    }
    public void setUpErase()
    {
        ImageButton eraseButton = (ImageButton) findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener(v -> drawingView.setEraseMode());
    }

    public void setUpUndo()
    {
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(v -> {
            drawingView.undo();
        });
    }

    public void setUpRedo()
    {
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        redoButton.setOnClickListener(v -> {
            drawingView.redo();
        });
    }

    public void setUpSmudge()
    {
        ImageButton smudgeButton = (ImageButton) findViewById(R.id.smudgeButton);
        smudgeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawingView.setSmudgeMode();

                //drawingView.setLayerType(layerType, )
            }
        });
    }


    public void updateVisibility(boolean isAvailable, ImageButton button)
    {
        if (isAvailable) {
            button.setVisibility(View.VISIBLE);
        }
        else {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUndoAvailable(boolean isAvailable) {
        updateVisibility(isAvailable, undoButton);
    }

    @Override
    public void onRedoAvailable(boolean isAvailable) {
        updateVisibility(isAvailable, redoButton);
    }
}
