package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;

    private float previousX;
    private float previousY;
    private CharcoalTool charcoalTool;

    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        charcoalTool = new CharcoalTool();
        initializeDrawing();
    }

    private void initializeDrawing() {
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
                //TODO: need to be able to adjust circle size and opacity in this class
                //really, I want to set the opacity in charcoal, since that's where the values are known
                //but the paint here is overriding any colors, how can I insert the circle as more of an image
                //with pre-set colors?
                printCircleWithLocation(pointX, pointY);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_MOVE:
                printCircleWithLocation(pointX, pointY);
                drawContinuouslyBetweenPoints(pointX, pointY, previousX, previousY);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        //a change invalidated view layout --> onDraw method executes
        invalidate();
        return true;
    }

    /**
     * Adapted from https://stackoverflow.com/a/34142336/2700520
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void drawContinuouslyBetweenPoints(float x1, float y1, float x2, float y2)
    {
        final float RADIUS = 10.0f;
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        float slope = (dx == 0) ? 0 : dy/dx;

        float times = distance / RADIUS - 1;
        for (float i = 0; i < times; i++)
        {
            float yIncrement = slope == 0 && dx != 0 ? 0 : dy * ( i /times);
            float xIncrement = slope == 0 ? dx * (i / times ) : yIncrement / slope;
            bitmapCanvas.drawBitmap(charcoalTool.getBitmap(), x1 + xIncrement, y1 + yIncrement, paint);
        }

        if (times <= 0)
        {
            bitmapCanvas.drawBitmap(charcoalTool.getBitmap(), x1, y1, paint);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int previousHeight, int previousWidth) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    public Paint getPaint() {
        return paint;
    }

    private void printCircleWithLocation(float pointX, float pointY)
    {
        bitmapCanvas.drawBitmap(charcoalTool.getBitmap(), pointX, pointY, paint);
    }

    public void setRadius(int value)
    {
        charcoalTool.setRadius(value);
    }
}
