package com.sschuraytz.charcoaldrawing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Log.d("SPLASH_TEST", "Entered Splash Activity");
        startActivity(intent);
        finish();
    }
}
