package com.sschuraytz.charcoaldrawing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class VoiceCommands {
    private Activity activity;
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
        public void smudgeCommand() {

        }

        @Override
        public void eraserCommand() {

        }

        @Override
        public boolean undoCommand() {
            return false;
        }

        @Override
        public boolean redoCommand() {
            return false;
        }

        @Override
        public void createNewCanvasCommand() {

        }

        @Override
        public void updateDrawingThickness(int num) {

        }

        @Override
        public void saveDrawing() {

        }

        @Override
        public void help() {

        }

        @Override
        public void lighter() {

        }

        @Override
        public void darker() {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public VoiceCommands(Activity baseActivity) {
        activity = baseActivity;
        checkVoicePermissions();
        setUpRecognitionListener();
        setUpSpeechRecognizer();
    }

    public void setUpSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    public void setUpRecognitionListener() {
        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(activity, "listening", Toast.LENGTH_SHORT).show();
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
                    // only save first word from user command
                    String result = Arrays.asList(data.get(0).split(" ")).get(0);
                    switch (result) {
                        case "new":
                        case "clear":
                            //if new, need to prompt user to save current drawing first
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            voiceListener.createNewCanvasCommand();
                            break;
                        case "charcoal":
                        case "draw":
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            voiceListener.charcoalCommand();
                            break;
                        case "smudge":
                        case "blend":
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            voiceListener.smudgeCommand();
                            break;
                        case "eraser":
                        case "erase":
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            voiceListener.eraserCommand();
                            break;
                        case "undo":
                            if (voiceListener.undoCommand()) {
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(activity, "nothing to undo", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "redo":
                            if (voiceListener.redoCommand()) {
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(activity, "nothing to redo", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "save":
                        case "safe":   //speech recognizer sometimes interprets "save" as "safe"
                            voiceListener.saveDrawing();
                            break;
                        case "help":
                            voiceListener.help();
                            break;
                        case "lighter":
                        case "later":
                        case "liker":
                        case "spider":
                            voiceListener.lighter();
                            break;
                        case "darker":
                            voiceListener.darker();
                            break;
                        case "hundred":
                            Toast.makeText(activity, "100", Toast.LENGTH_SHORT).show();
                            voiceListener.updateDrawingThickness(100);
                            break;
                        default:
                            // slider/radius
                            if (StringUtils.isNumeric(result)) {
                                int numericResult = Integer.parseInt(result);
                                if (numericResult <= 0 || numericResult > 100) {
                                    Toast.makeText(activity, "Width must be between 1 and 100", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                                    voiceListener.updateDrawingThickness(numericResult);
                                }
                            }
                            else {
                                Toast.makeText(activity, "no such command", Toast.LENGTH_SHORT).show();
                            }
                    }
                }
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
        // info about next attribute say it's best not to modify the value
        // also, I'm  not sure it's making a difference
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10);
        //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        speechRecognizer.startListening(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkVoicePermissions () {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] { Manifest.permission.RECORD_AUDIO},
                    1
            );
        }
    }

    public void setListener(VoiceListener voiceListener) {
        this.voiceListener = voiceListener;
    }
}
