package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class DrawingView extends View {

    //drawing primitive
    private Path path;
    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;
    private Random rand = new Random();
    private int radius;


    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        initializeDrawing();
    }

    private void initializeDrawing() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //make line strokes instead of painting area
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                printTexturedCircle(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(pointX, pointY);
                printTexturedCircle(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                bitmapCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }
        //a change invalidated view layout --> onDraw method executes
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int height, int width, int previousHeight, int previousWidth) {
        bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
        //this is the place to experiment with different charcoal textures, I think
    }

    public Paint getPaint() {
        return paint;
    }

    private void printTexturedCircleBorder(float pointX, float pointY, float maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        for (int angle = 0; angle < 360; angle+=20)
        {
            horizontalShift = (float) (maxMagnitude * Math.cos(angle));
            verticalShift = (float) (maxMagnitude * Math.sin(angle));
            paint.setAlpha(255 - (int)maxMagnitude);
            bitmapCanvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
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
        int maxMagnitude = radius * 3;
        float horizontalShift;
        float verticalShift;
        for (int i = 0; i < 20; i++)
        {
            angle = rand.nextInt(350);
            magnitude = rand.nextInt(maxMagnitude);
            horizontalShift = (float) (magnitude * Math.cos(angle));
            verticalShift = (float) (magnitude * Math.sin(angle));
            paint.setAlpha(255 - magnitude);
            bitmapCanvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
        }
        printTexturedCircleBorder(pointX, pointY, maxMagnitude);
    }

    public void setRadius(int value)
    {
        radius = value;
    }
}
