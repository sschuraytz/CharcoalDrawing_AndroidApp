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

/**
 * when click down, "grab" pixels in radius (circular) around finger touch
 * something needs to hold the pixels
 * some of the pixels from the initial place of contact are dragged across the screen
 * they become lighter and more blurred
 */

//creating circle based on this algorithm: https://stackoverflow.com/a/32191523
public class SmudgeToolBitmapShader extends Tool {

    protected Bitmap croppedBitmap;
    protected RectF croppedRect;
    protected Paint smudgePaint;
    protected int localWidth;
    protected int localHeight;
    protected int startX;
    protected int startY;

    public SmudgeToolBitmapShader() {
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
        smudgePaint = setUpPaint();
    }

    @Override
    protected void drawSinglePoint(Canvas bitmapCanvas, float x, float y) {
        if (smudgePaint.getAlpha() > 10) {
            smudgePaint.setAlpha(smudgePaint.getAlpha() - 10);
        }
        int radius = this.getRadius();
        RectF rect = new RectF(x - radius/2, y - radius/2, x + radius/2, y + radius/2);
        bitmapCanvas.drawRoundRect(rect, this.getRadius(), this.getRadius(), smudgePaint);
    }

    @Override
    protected void drawContinuouslyBetweenPoints(Canvas bitmapCanvas, float x1, float y1, float x2, float y2) {
        super.drawContinuouslyBetweenPoints(bitmapCanvas, x1, y1, x2, y2);
    }

    //https://stackoverflow.com/a/32191523
    public Paint setUpPaint() {
        //total solid
       // BitmapShader shader = new BitmapShader(croppedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //copies textured pattern, but then it's repeating it --> patterned screen instead of blended
        BitmapShader shader = new BitmapShader(croppedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setShader(shader);
        return localPaint;
    }

}
