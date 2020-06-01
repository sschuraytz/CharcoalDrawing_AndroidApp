package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class CharcoalTool{

    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private Random rand = new Random();
    private int radius;

    public CharcoalTool() {
        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(10);
        createCircleWithNoLocation();
    }

    private void createCircleWithNoLocation()
    {
        printTexturedCircle(50, 50);
    }

    private void printTexturedCircleBorder(float pointX, float pointY, int maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        for (int angle = 0; angle < 360; angle+=20)
        {
            horizontalShift = (float) (maxMagnitude * Math.cos(angle));
            verticalShift = (float) (maxMagnitude * Math.sin(angle));
            paint.setAlpha(255 - maxMagnitude);
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
            if ( angle % 3 == 0)
            {
                angle+=20;
            }
        }
    }

    private void printTexturedCircle(float pointX, float pointY)
    {
        int angle;
        int magnitude;
        //int maxMagnitude = radius * 3;
        int maxMagnitude = 50;
        float horizontalShift;
        float verticalShift;
        for (int i = 0; i < 20; i++)
        {
            angle = rand.nextInt(360);
            magnitude = rand.nextInt(maxMagnitude);
            horizontalShift = (float) (magnitude * Math.cos(angle));
            verticalShift = (float) (magnitude * Math.sin(angle));
            paint.setAlpha(255 - magnitude);
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
        }
        printTexturedCircleBorder(pointX, pointY, maxMagnitude);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
