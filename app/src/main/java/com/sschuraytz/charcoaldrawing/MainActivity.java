package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements UndoRedoListener {

    private SeekBar drawingThickness;
    private DrawingView drawingView;
    private ImageButton undoButton;
    private ImageButton redoButton;
    private ImageButton newButton;
   // private RecognitionListener recognitionListener;
   // private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        //setUpSpeechRecognizer();
        //setUpRecognitionListener();
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

  //  @RequiresApi(api = Build.VERSION_CODES.M)
   /* public void checkVoicePermissions () {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            perform();
        } else if (shouldShowRequestPermissionRationale()) {
            showInContextUI();
        } else {
            requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO},
                    Define.)
        }
    }*/

/*    public void setUpSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    }*/

   /* public void setUpRecognitionListener() {
        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

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

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };
    }*/
/*    public void setUpRedo()
    {
        speechRecognizer.setRecognitionListener(recognitionListener);
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        redoButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            speechRecognizer.startListening(intent);
        });
    }*/
}
