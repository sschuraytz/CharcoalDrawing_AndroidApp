package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * when click down, "grab" pixels in radius around finger touch
 * something needs to hold the pixels
 * some of the pixels from the initial place of contact are dragged across the screen
 * they become lighter and more blurred
 * need to be careful that playing with the bitmap doesn't mess up "new/clear" command -
 * right now, it's causing that command to be executed on next ACTION_DOWN instead of immediately
 */

public class SmudgeTool extends Tool {

    protected Bitmap croppedBitmap;
    protected Paint paint;

    public SmudgeTool() {
        super(Color.argb(15, 49, 51, 53));
    }

    /**
     * @param bitmapCanvas
     * @param inputBitmap
     * @param x1
     * @param y1
     */
    @Override
    protected void onDown(Canvas bitmapCanvas, Bitmap inputBitmap, float x1, float y1) {
        int radius = this.getRadius();
        croppedBitmap = Bitmap.createBitmap(inputBitmap, (int) (x1 - radius / 2), (int) (y1 - radius / 2), radius, radius);

    }

    @Override
    protected void drawContinuouslyBetweenPoints(Canvas bitmapCanvas, float x1, float y1, float x2, float y2) {
        // bitmapCanvas.drawColor(Color.WHITE);
        bitmapCanvas.drawBitmap(croppedBitmap, x1, y1, paint);
        //super.printToCanvas(bitmapCanvas, x1, y1, paint);
        // super.drawContinuouslyBetweenPoints(bitmapCanvas, x1 - radius, y1 - radius, x1 - radius + croppedBitmap.getWidth(), y1 - radius + croppedBitmap.getHeight(), paint);
    }

}
