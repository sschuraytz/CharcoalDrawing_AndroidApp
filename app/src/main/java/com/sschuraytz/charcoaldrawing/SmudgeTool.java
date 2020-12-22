package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * when click down, "grab" pixels in radius (circular) around finger touch
 * something needs to hold the pixels
 * some of the pixels from the initial place of contact are dragged across the screen
 * they become lighter and more blurred
 */

//creating circle based on this algorithm: https://stackoverflow.com/a/13737939
public class SmudgeTool extends Tool {

    protected Bitmap croppedBitmap;
    protected Bitmap circularBitmap;
    protected RectF croppedRect;
    protected int localWidth;
    protected int localHeight;
    protected int startX;
    protected int startY;

    public SmudgeTool() {
        super(Color.argb(15, 49, 51, 53));
        croppedRect = new RectF();
    }

    /**
     * @param bitmapCanvas
     * @param inputBitmap
     * @param x1
     * @param y1
     */
    @Override
    protected void onDown(Canvas bitmapCanvas, Bitmap inputBitmap, float x1, float y1) {
        paint.setAlpha((int) (this.getOpacity() / 2.5));
        int radius = this.getRadius();
        if (croppedBitmap != null) {
            croppedBitmap.recycle();
        }
        startX = x1 > (radius/2) ? (int)(x1 - radius/2) : (int)x1;
        startY = y1 > (radius/2) ? (int)(y1 - radius/2) : (int)y1;
        localWidth = startX + (radius*2) > inputBitmap.getWidth() ? inputBitmap.getWidth() - startX - 1 : radius * 2;
        localHeight = startY + (radius*2) > inputBitmap.getHeight() ? inputBitmap.getHeight() - startY - 1 : radius * 2;
        croppedBitmap = Bitmap.createBitmap(inputBitmap, startX, startY, localWidth, localHeight);
        circularBitmap = getCircularBitmap(croppedBitmap);
    }

    @Override
    protected void drawSinglePoint(Canvas bitmapCanvas, float x, float y) {
        paint.setAlpha(Math.max(0, paint.getAlpha() - 5));
        bitmapCanvas.drawBitmap(circularBitmap, x, y, paint);
    }

    //not quite producing the effect I want. it's printing new circles instead of blurring what exists on the canvas.
    // it's even less natural than the charcoal
    // looks ok on emulator b/c it's small, but does not look great on actual device
   // https://stackoverflow.com/a/13737939
    private Bitmap getCircularBitmap(Bitmap inBitmap) {
        Bitmap finalBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);

        Paint localPaint = new Paint();
        Rect finalRect = new Rect(0, 0, inBitmap.getWidth(), inBitmap.getHeight());
        canvas.drawCircle(inBitmap.getWidth()/2, inBitmap.getHeight()/2, inBitmap.getWidth()/2, localPaint);
        localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, finalRect, finalRect, localPaint);
        return finalBitmap;
    }

        //TODO? change smudge to update currentBitmap as drag so can smudge if reach dark spot even if first touch was white

    /* saved experimentation from drawContinuouslyBetweenPoints()

        RectF rect = new RectF(x1, y1, localWidth, localHeight);
        bitmapCanvas.drawRoundRect(rect, this.getRadius(), this.getRadius(), localPaint);
    //  smudgePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  --> this was drawing squares
    //  croppedRect = new RectF(x1 - this.getRadius()/2, y1 - this.getRadius()/2, x1 + this.getRadius()/2, y1 + this.getRadius()/2);
    //  bitmapCanvas.drawRoundRect(croppedRect, croppedRect.height()/2, croppedRect.height()/2, smudgePaint);
    //  super.drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x1 + croppedBitmap.getWidth(), y1 + croppedBitmap.getHeight());

     */
}
