package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import java.util.Random;
import java.util.Stack;


public class Tool{

    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private static final Random rand = new Random();
    private int radius;
    private int opacity = 225;


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

    /**
     * Adapted from https://stackoverflow.com/a/34142336/2700520
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected void drawContinuouslyBetweenPoints(Canvas bitmapCanvas, float x1, float y1, float x2, float y2)
    {
        final float RADIUS = 10.0f;
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        float slope = (dx == 0) ? 0 : dy/dx;

        float times = distance / RADIUS - 1;
        //ensure thin line has more dots since it doesn't have much overlap
        float incrementer = getRadius() > 5 ? 1 : 0.5f;
        for (float i = 0; i < times; i+=incrementer)
        {
            float yIncrement = slope == 0 && dx != 0 ? 0 : dy * ( i /times);
            float xIncrement = slope == 0 ? dx * (i / times ) : yIncrement / slope;
            bitmapCanvas.drawBitmap(bitmap,
                    x1 + xIncrement - getRadius(),
                    y1 + yIncrement - getRadius(),
                    paint);
        }

        if (times <= 0)
        {
            bitmapCanvas.drawBitmap(bitmap,
                    x1 - getRadius(),
                    y1 - getRadius(),
                    paint);
        }
    }

    private void printTexturedCircleBorder(float pointX, float pointY, int maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        //paint.setAlpha(255 - maxMagnitude*2);
        paint.setAlpha(opacity - maxMagnitude);

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
        //paint.setAlpha(255);
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

    public void setRadius(int value)
    {
        radius = value;
        printTexturedCircle();
    }

    public int getRadius()
    {
        return radius;
    }

    public void setOpacity(int value)
    {
        opacity = value;
        printTexturedCircle();
    }

    public int getOpacity() {
        return opacity;
    }
}
