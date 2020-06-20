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

    public SmudgeTool() {
        super(Color.argb(15, 49,51, 53));
    }

    @Override
    protected void printToCanvas(Canvas bitmapCanvas, float pointX, float pointY, Paint paint) {
        getSurroundingPixels(bitmapCanvas, pointX, pointY, paint);
        //super.printToCanvas(bitmapCanvas, pointX, pointY, paint);
    }

    public void getSurroundingPixels(Canvas bitmapCanvas, float pointX, float pointY, Paint paint) {
        int halfRadius = radius/2;
        Rect src = new Rect((int)(pointX) - halfRadius, (int)pointY - halfRadius, (int)pointX + halfRadius, (int)pointY + halfRadius);
/*        Rect dest = new Rect((int)(pointX) - radius, (int)pointY - radius, (int)pointX + radius, (int)pointY + radius);
        bitmapCanvas.drawBitmap(bitmap, src, dest, paint);*/
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmapCanvas.drawBitmap(croppedBitmap, pointX, pointY, paint);
    }
}
