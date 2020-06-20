package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import java.util.Random;


public class Tool{

    protected Canvas canvas;
    protected Bitmap bitmap;
    protected Paint paint;
    public static final Random rand = new Random();
    protected int radius;

    public Tool(int color) {
        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //should allow user to modify
        paint.setStrokeWidth(3);
        radius = 35;
        paint.setColor(color);
        printTexturedCircle();
    }

    private void printTexturedCircleBorder(float pointX, float pointY, int maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        paint.setAlpha(255 - maxMagnitude*2);

        //small increment value for large radius so shape is more circular --> end of line more smooth
        float incrementer = radius > 80 ?(0.125f*radius) : radius + 1;
        for (int angle = 0; angle < 360; angle+=incrementer)
        {
            horizontalShift = (float) (maxMagnitude * Math.cos(angle));
            verticalShift = (float) (maxMagnitude * Math.sin(angle));
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
        }
    }

    protected void printTexturedCircle(int density)
    {
        int angle;
        int magnitude;
        int maxMagnitude = radius;
        float pointX = radius;
        float pointY = radius;
        float horizontalShift;
        float verticalShift;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setAlpha(255);
        for (int i = 0; i < radius*2*density; i++)
        {
            angle = rand.nextInt(360);
            //lower bound to avoid having too many dots in circle center which creates dark center line
            magnitude = rand.nextInt(maxMagnitude - (maxMagnitude/6)) + (maxMagnitude/6);
            horizontalShift = (float) (magnitude * Math.cos(angle));
            verticalShift = (float) (magnitude * Math.sin(angle));
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
        }

        printTexturedCircleBorder(pointX, pointY, maxMagnitude);
    }

    protected void printTexturedCircle()
    {
        printTexturedCircle(1);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setRadius(int value)
    {
        radius = value;
        printTexturedCircle();
    }

    public int getRadius()
    {
        return radius;
    }

}
