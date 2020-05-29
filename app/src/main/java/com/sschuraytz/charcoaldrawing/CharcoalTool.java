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
        bitmap = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        createCircleWithNoLocation();
       // onDraw(canvas);
    }

//    public void onDraw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, 0, 0, paint);
//    }

    private void createCircleWithNoLocation()
    {
        int angle;
        int magnitude;
        int maxMagnitude = 360;
        //int maxMagnitude = radius * 3;
        float horizontalShift;
        float verticalShift;
        for (int i = 0; i < 20; i++)
        {
            angle = rand.nextInt(maxMagnitude);
            //magnitude = rand.nextInt(maxMagnitude);
            magnitude = rand.nextInt(300);
            horizontalShift = (float) (magnitude * Math.cos(angle));
            verticalShift = (float) (magnitude * Math.sin(angle));
            //paint.setAlpha(255 - magnitude);
           // canvas.drawPoint(horizontalShift, verticalShift, paint);
            //canvas.drawPoint(5, 5, paint);
        }
       // printTexturedCircleBorder(pointX, pointY, maxMagnitude);
        printTexturedCircleBorder(0, 0, 100);

    }

    //TODO: this needs to be circular. the code didn't change, but it's not going all the way around
    private void printTexturedCircleBorder(float pointX, float pointY, float maxMagnitude)
    {
        float horizontalShift;
        float verticalShift;
        for (int angle = 0; angle < 360; angle+=10)
        {
            horizontalShift = (float) (maxMagnitude * Math.cos(angle));
            verticalShift = (float) (maxMagnitude * Math.sin(angle));
            //paint.setAlpha(255 - (int)maxMagnitude);
            //canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
            canvas.drawPoint(pointX + horizontalShift, pointY + verticalShift, paint);
            if ( angle % 3 == 0)
            {
                angle+=20;
            }
        }
    }

/*    private void printTexturedCircle(float pointX, float pointY)
    {

        int angle;
        int magnitude;
        int maxMagnitude = radius * 3;
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
      //  printTexturedCircleBorder(pointX, pointY, maxMagnitude);
    }*/

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
