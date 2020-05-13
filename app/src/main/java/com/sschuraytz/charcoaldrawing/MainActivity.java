package com.sschuraytz.charcoaldrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar drawingThickness;
    private DrawingView drawingView;
    private ImageButton newButton;
    private ImageButton eraseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView) findViewById(R.id.canvas);
        setUpSlider();
        setUpOptionForNewCanvas();
        setUpErase();
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

    public void setUpOptionForNewCanvas()
    {
        newButton = (ImageButton) findViewById(R.id.newCanvasButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCanvas();
            }
        });
    }

    public void clearCanvas()
    {
        drawingView.getBitmapCanvas().drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawingView.getPaint().setColor(Color.BLACK);
        drawingView.getPaint().setXfermode(null);
    }

    public void setUpErase()
    {
        eraseButton = (ImageButton) findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startEraseMode();
            }
        });
    }

    public void startEraseMode()
    {
        //Paint erasePaint = new Paint(Color.WHITE);
        //drawingView.getBitmapCanvas().drawPaint(erasePaint);
        //drawingView.getBitmapCanvas().drawColor(0, PorterDuff.Mode.CLEAR);
        //drawingView.getPaint().setColor(0);
        drawingView.getPaint().setColor(Color.BLUE);
        drawingView.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
       // drawingView.getPaint().setColor(Color);
    }

}
