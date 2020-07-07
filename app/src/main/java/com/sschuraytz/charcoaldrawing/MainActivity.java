package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements UndoRedoListener {

    private SeekBar drawingThickness;
    private DrawingView drawingView;
    private ImageButton undoButton;
    private ImageButton redoButton;
    private ImageButton newButton;
    private RecognitionListener recognitionListener;
    private SpeechRecognizer speechRecognizer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkVoicePermissions();
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawingView();
        setUpOptionForNewCanvas();
        setUpSlider();
        setUpDraw();
        setUpErase();
        setUpUndo();
        setUpRedo();
        setUpFAB();
        setUpRecognitionListener();
        setUpSpeechRecognizer();
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

    public void setUpOptionForNewCanvas()
    {
        newButton = (ImageButton) findViewById(R.id.newCanvasButton);
        newButton.setOnClickListener(v -> drawingView.createNewCanvas());
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
        undoButton.setOnClickListener(v -> drawingView.undo());
    }

    public void setUpRedo()
    {
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        redoButton.setOnClickListener(v -> drawingView.redo());
    }

    public void updateVisibility(boolean isAvailable, ImageButton button)
    {
        if (isAvailable) {
            button.setVisibility(View.VISIBLE);
        }
        else {
            button.setVisibility(View.INVISIBLE);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkVoicePermissions () {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //perform();
        } else {
           requestPermissions(
               new String[] { Manifest.permission.RECORD_AUDIO},
                1
           );
        }
    }

    public void setUpSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    public void setUpRecognitionListener() {
        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(getApplicationContext(), "listening", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            //Is there a way to make it more flexible so if user doesn't say exact word it still works?
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                assert data != null;
                String result = data.get(0);
                if (result.equalsIgnoreCase("charcoal")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    drawingView.setDrawingMode();
                }
                else if (result.equalsIgnoreCase("eraser")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    drawingView.setEraseMode();
                }
                else if (result.equalsIgnoreCase("undo")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    drawingView.undo();
                }
                else if (result.equalsIgnoreCase("redo")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    drawingView.redo();
                }
                else if (result.equalsIgnoreCase("new")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    drawingView.createNewCanvas();
                }
                else {
                    Toast.makeText(getApplicationContext(), "no such command", Toast.LENGTH_SHORT).show();
                }
                //smudge
                //save
                //help
                //slider/radius - check if contains digit, if so, adjust slider
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };
    }
    public void listenToUserCommand()
    {
        //intent = simple message to transfer data btwn activities
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
      //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        speechRecognizer.startListening(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            checkVoicePermissions();
            listenToUserCommand();
        });
    }
}
