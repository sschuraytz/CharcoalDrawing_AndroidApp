package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import java.util.Random;


public class Tool{

    private Canvas canvas;
    private Bitmap bitmap;

    protected Paint paint;
    private static final Random rand = new Random();
    private int radius;
    private int opacity = 180; //possible alpha range: 0-255

    public Tool(int color) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setColor(color);
        setRadius(30);
    }

    protected void onDown(Canvas bitmapCanvas, Bitmap inputBitmap, float x1, float y1) {
        drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x1, y1);
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
            drawSinglePoint(bitmapCanvas, x1 + xIncrement, y1 + yIncrement);
        }

        if (times <= 0)
        {
            drawSinglePoint(bitmapCanvas, x1, y1);
        }
    }
    //this draws an entire textured circle of dots
    protected void drawSinglePoint(Canvas bitmapCanvas, float x, float y) {
        bitmapCanvas.drawBitmap(bitmap,
                x - getRadius(),
                y - getRadius(),
                paint);
    }

    private void printTexturedCircleBorder(float pointX, float pointY, int maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        //make the edge of each circle lighter than the center so resulting line is lighter at the edges
        paint.setAlpha(opacity - radius);
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
        float pointX = radius;
        float pointY = radius;
        float horizontalShift;
        float verticalShift;
        //the following line clears the canvas that the tool "stamp" is drawn on
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < radius*2*density; i++)
        {
            angle = rand.nextInt(360);
            //lower bound to avoid having too many dots in circle center which creates dark center line
            magnitude = rand.nextInt(radius - (radius/6)) + (radius/6);
            //make dots closer to the edge of each circle lighter so resulting line is lighter at the edges
            paint.setAlpha(opacity - magnitude);
            horizontalShift = (float) (magnitude * Math.cos(angle));
            verticalShift = (float) (magnitude * Math.sin(angle));
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
        }

        printTexturedCircleBorder(pointX, pointY, radius);
    }

    protected void printTexturedCircle()
    {
        printTexturedCircle(1);
    }

    public void setRadius(int value)
    {
        radius = value;
        setUpCanvas();  //set up canvas large enough to accommodate circle with new radius
    }

    public void setUpCanvas() {
        bitmap = Bitmap.createBitmap(radius*2, radius*2, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        printTexturedCircle();
    }

    public int getRadius()
    {
        return radius;
    }

    public void incrementOpacity(int value)
    {
        opacity += value;
        printTexturedCircle();
    }

    public int getOpacity() {
        return opacity;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
