package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

/**
 * when click down, "grab" pixels in radius (circular) around finger touch
 * something needs to hold the pixels
 * some of the pixels from the initial place of contact are dragged across the screen
 * they become lighter and more blurred
 */

public class SmudgeTool extends Tool {

    protected Bitmap croppedBitmap;
    protected RectF croppedRect;
    protected Paint smudgePaint;
    protected int localWidth;
    protected int localHeight;
    protected int startX;
    protected int startY;

    public SmudgeTool() {
        super(Color.argb(15, 49, 51, 53));
        smudgePaint = new Paint();
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
        smudgePaint.setAlpha(this.getOpacity());
        int radius = this.getRadius();
        if (croppedBitmap != null) {
            croppedBitmap.recycle();
        }
        startX = x1 > (radius/2) ? (int)(x1 - radius/2) : (int)x1;
        startY = y1 > (radius/2) ? (int)(y1 - radius/2) : (int)y1;
        localWidth = startX + radius > inputBitmap.getWidth() ? inputBitmap.getWidth() - startX - 1 : radius;
        localHeight = startY + radius > inputBitmap.getHeight() ? inputBitmap.getHeight() - startY - 1 : radius;
        croppedBitmap = Bitmap.createBitmap(inputBitmap, startX, startY, localWidth, localHeight);
    }

    @Override
    protected void drawSinglePoint(Canvas bitmapCanvas, float x, float y) {
        //seems to repeat calculations from onDown, & could run into same bounds issues.
        int radius = this.getRadius();
        RectF rect = new RectF(x - radius/2, y - radius/2, x + radius/2, y + radius/2);
     ///this line is why it wasn't getting lighter:
      ///   smudgePaint = setUpPaint(bitmapCanvas);
       // smudgePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
     //   bitmapCanvas.drawRoundRect(rect, this.getRadius(), this.getRadius(), smudgePaint);

        /*bitmapCanvas.drawBitmap(croppedBitmap,
                x - getRadius(),
                y - getRadius(),
                smudgePaint);*/
        if (smudgePaint.getAlpha() > 10) {
            smudgePaint.setAlpha(smudgePaint.getAlpha() - 10);
        }
        bitmapCanvas.drawBitmap(getCircularBitmap(croppedBitmap), x, y, smudgePaint);
    }

    @Override
    protected void drawContinuouslyBetweenPoints(Canvas bitmapCanvas, float x1, float y1, float x2, float y2) {
        super.drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x2, y2);

/*


        RectF rect = new RectF(x1, y1, localWidth, localHeight);
        bitmapCanvas.drawRoundRect(rect, this.getRadius(), this.getRadius(), localPaint);*/


     //   bitmapCanvas.drawBitmap(croppedBitmap, x1, y1, smudgePaint);

   ///Nov. 1 4:56     smudgePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  --> this was drawing squares
      //  croppedRect = new RectF(x1 - this.getRadius()/2, y1 - this.getRadius()/2, x1 + this.getRadius()/2, y1 + this.getRadius()/2);
      //  bitmapCanvas.drawRoundRect(croppedRect, croppedRect.height()/2, croppedRect.height()/2, smudgePaint);
      ////////////////////////////
        ///////////////bitmapCanvas.drawBitmap(getCircularBitmap(croppedBitmap), x1, y1, smudgePaint);
   ///      super.drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x1 + croppedBitmap.getWidth(), y1 + croppedBitmap.getHeight());
         //super.drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x1 + croppedBitmap.getWidth(), y1 + croppedBitmap.getHeight(), smudgePaint);


    }
//this works with line 60
    //but even this is not producing the effect I want. it's printing new circles instead of blurring what exists on the canvas.
    // it's even less natural than the charcoal
   // https://stackoverflow.com/questions/13285661/android-cut-a-circle-from-a-square-bitmap/32191523#32191523
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

    public Paint setUpPaint(Canvas bitmapCanvas) {
        //total solid
       // BitmapShader shader = new BitmapShader(croppedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //copies textured pattern, but then it's repeating it --> patterned screen instead of blended
        BitmapShader shader = new BitmapShader(croppedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setShader(shader);
     //   bitmapCanvas.drawCircle(100, 100, this.getRadius(), localPaint);
        return localPaint;
    }

    //TODO: right now it's drawing filled circles of varying colors (grayscale). need textured!
}
