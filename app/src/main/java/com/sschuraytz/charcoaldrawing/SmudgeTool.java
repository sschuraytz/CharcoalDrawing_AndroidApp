package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * when click down, "grab" pixels in radius around finger touch
 * something needs to hold the pixels
 */

public class SmudgeTool extends Tool {

    protected Bitmap bitmap;
    protected Paint paint;

    public SmudgeTool() {
        super(Color.argb(15, 49,51, 53));
    }

    @Override
    protected void drawContinuouslyBetweenPoints(Canvas bitmapCanvas, float x1, float y1, float x2, float y2) {
        Bitmap croppedBitmap = getSurroundingPixels(x1, y1, x2, y2);
        bitmapCanvas.drawBitmap(croppedBitmap, x1, y1, paint);
        //super.printToCanvas(bitmapCanvas, x1, y1, paint);
       // super.drawContinuouslyBetweenPoints(bitmapCanvas, x1 - radius, y1 - radius, x1 - radius + croppedBitmap.getWidth(), y1 - radius + croppedBitmap.getHeight(), paint);
    }

/*    @Override
    protected void printToCanvas(Canvas bitmapCanvas, float pointX, float pointY, Paint paint) {
     //   Bitmap croppedBitmap = getSurroundingPixels(bitmapCanvas, pointX, pointY, paint);
     //   bitmapCanvas.drawBitmap(croppedBitmap, pointX, pointY, paint);
        //super.printToCanvas(bitmapCanvas, pointX, pointY, paint);
    }*/

    public Bitmap getSurroundingPixels(float x1, float y1, float x2, float y2) {
        bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        bitmap.setHeight(500);
        return Bitmap.createBitmap(bitmap, (int)0, (int)0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        //int halfRadius = radius/2;
        //Rect src = new Rect((int)(x1), (int)pointY - halfRadius, (int)pointX + halfRadius, (int)pointY + halfRadius);
    }

/*    public Bitmap getSurroundingPixels(Canvas bitmapCanvas, float pointX, float pointY, Paint paint) {
        int halfRadius = radius/2;
        Rect src = new Rect((int)(pointX) - halfRadius, (int)pointY - halfRadius, (int)pointX + halfRadius, (int)pointY + halfRadius);
*//*        Rect dest = new Rect((int)(pointX) - radius, (int)pointY - radius, (int)pointX + radius, (int)pointY + radius);
        bitmapCanvas.drawBitmap(bitmap, src, dest, paint);*//*
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return croppedBitmap;
        //bitmapCanvas.drawBitmap(croppedBitmap, pointX, pointY, paint);
        //bitmapCanvas.drawBitmap(croppedBitmap, pointX + 25, pointY + 25, paint);
    }*/
}
