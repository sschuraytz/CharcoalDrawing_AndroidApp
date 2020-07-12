package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class VoiceCommands {
    private Activity context;
    private RecognitionListener recognitionListener;
    private SpeechRecognizer speechRecognizer;
    private VoiceListener voiceListener = new VoiceListener() {
        @Override
        public void updateFABUI() {

        }

        @Override
        public void charcoalCommand() {

        }

        @Override
        public void eraserCommand() {

        }

        @Override
        public void undoCommand() {

        }

        @Override
        public void redoCommand() {

        }

        @Override
        public void createNewCanvasCommand() {

        }

        @Override
        public void updateDrawingThickness(int num) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public VoiceCommands(Activity baseContext) {
        context = baseContext;
        checkVoicePermissions();
        setUpRecognitionListener();
        setUpSpeechRecognizer();
    }

    public void setUpSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    public void setUpRecognitionListener() {
        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(context, "listening", Toast.LENGTH_SHORT).show();
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
                voiceListener.updateFABUI();
            }

            @Override
            public void onError(int error) {
                voiceListener.updateFABUI();
            }

            //Is there a way to make it more flexible so if user doesn't say exact word it still works?
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null) {
                    String result = data.get(0);
                    switch (result) {
                        case "charcoal":
                        case "draw":
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            voiceListener.charcoalCommand();
                            break;
                        case "eraser":
                        case "erase":
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            voiceListener.eraserCommand();
                            break;
                        case "undo":
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            voiceListener.undoCommand();
                            break;
                        case "redo":
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            voiceListener.redoCommand();
                            break;
                        case "new":
                        case "new canvas":
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                            voiceListener.createNewCanvasCommand();
                            break;
                        default:
                            // slider/radius
                            if (isNumber(result)) {
                                if (Integer.parseInt(result) < 0 || Integer.parseInt(result) > 100) {
                                    Toast.makeText(context, "Width must be between 0 and 100", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                                voiceListener.updateDrawingThickness(Integer.parseInt(result));
                            }
                            else {
                                Toast.makeText(context, "no such command", Toast.LENGTH_SHORT).show();
                            }
                    }
                    //smudge
                    //save
                    //help
                }
            }

            public boolean isNumber(String str) {
                char[] chars = str.toCharArray();
                for (char ch : chars) {
                    if (!Character.isDigit(ch)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
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
    public void checkVoicePermissions () {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(
                    context,
                    new String[] { Manifest.permission.RECORD_AUDIO},
                    1
            );
        }
    }

    public void setListener(VoiceListener voiceListener) {
        this.voiceListener = voiceListener;
    }

}
