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

    public SeekBar drawingThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingThickness = (SeekBar) findViewById(R.id.thicknessSlider);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedLayout = inflater.inflate(R.layout.activity_main, null);

        //SeekBar thicknessSlider = inflatedLayout.findViewById(R.id.thicknessSlider);

       // setUp();
       // setupSlider();
    }

  /*  public void setUp()
    {
        drawingThickness = (SeekBar) findViewById(R.id.thicknessSlider);
        drawingThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
            }
        });
    }*/


}
