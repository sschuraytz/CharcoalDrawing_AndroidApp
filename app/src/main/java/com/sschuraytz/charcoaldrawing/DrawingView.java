package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawingView extends View {

    //drawing primitive
    private Path path;
    //drawing style
    private Paint backgroundPaint;
    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;

    private boolean isEraseMode;


    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        initializeDrawing();
    }

    private void initializeDrawing() {
        path = new Path();
        //make line strokes instead of painting area
        setPaint();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                if (isEraseMode)
                {
                    bitmapCanvas.drawPath(path, paint);
                    path.reset();
                    path.moveTo(pointX, pointY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isEraseMode) {
                    bitmapCanvas.drawPath(path, paint);
                    path.reset();
                }
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
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
        canvas.drawPath(path, paint);
        //this is the place to experiment with different charcoal textures, I think
    }

    public Path getPath() {
        return path;
    }

    private void setPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Paint getPaint() {
        return paint;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public Canvas getBitmapCanvas() {
        return bitmapCanvas;
    }

    public boolean getEraseMode() {
        return isEraseMode;
    }

    public void setEraseMode(boolean isOn) {
        isEraseMode = isOn;
    }
}
