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
    private EraseTool eraseTool;
    private boolean isEraseMode;

    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        charcoalTool = new CharcoalTool();
        eraseTool = new EraseTool();
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
                if (isEraseMode)
                {
                    printEraseWithLocation(pointX, pointY);
                }
                else {
                    printCircleWithLocation(pointX, pointY);
                }
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isEraseMode)
                {
                    printEraseWithLocation(pointX, pointY);
                    eraseContinuouslyBetweenPoints(pointX, pointY, previousX, previousY);
                }
                else {
                    printCircleWithLocation(pointX, pointY);
                    drawContinuouslyBetweenPoints(pointX, pointY, previousX, previousY);
                }
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
        //ensure thin line has more dots since it doesn't have much overlap
        float incrementer = charcoalTool.getRadius() > 5 ? 1 : 0.5f;
        for (float i = 0; i < times; i+=incrementer)
        {
            float yIncrement = slope == 0 && dx != 0 ? 0 : dy * ( i /times);
            float xIncrement = slope == 0 ? dx * (i / times ) : yIncrement / slope;
            bitmapCanvas.drawBitmap(charcoalTool.getBitmap(),
                    x1 + xIncrement - charcoalTool.getRadius(),
                    y1 + yIncrement - charcoalTool.getRadius(),
                    paint);
        }

        if (times <= 0)
        {
            bitmapCanvas.drawBitmap(charcoalTool.getBitmap(),
                    x1 - charcoalTool.getRadius(),
                    y1 - charcoalTool.getRadius(),
                    paint);
        }
    }

    private void eraseContinuouslyBetweenPoints(float x1, float y1, float x2, float y2)
    {
        final float RADIUS = 10.0f;
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        float slope = (dx == 0) ? 0 : dy/dx;

        float times = distance / RADIUS - 1;
        //ensure thin line has more dots since it doesn't have much overlap
        float incrementer = eraseTool.getRadius() > 5 ? 1 : 0.5f;
        for (float i = 0; i < times; i+=incrementer)
        {
            float yIncrement = slope == 0 && dx != 0 ? 0 : dy * ( i /times);
            float xIncrement = slope == 0 ? dx * (i / times ) : yIncrement / slope;
            bitmapCanvas.drawBitmap(eraseTool.getBitmap(),
                    x1 + xIncrement - eraseTool.getRadius(),
                    y1 + yIncrement - eraseTool.getRadius(),
                    paint);
        }

        if (times <= 0)
        {
            bitmapCanvas.drawBitmap(eraseTool.getBitmap(),
                    x1 - eraseTool.getRadius(),
                    y1 - eraseTool.getRadius(),
                    paint);
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

    private void printCircleWithLocation(float pointX, float pointY)
    {
        bitmapCanvas.drawBitmap(charcoalTool.getBitmap(),
                pointX - charcoalTool.getRadius(),
                pointY - charcoalTool.getRadius(),
                paint);
    }

    private void printEraseWithLocation(float pointX, float pointY)
    {
        bitmapCanvas.drawBitmap(eraseTool.getBitmap(),
                pointX - eraseTool.getRadius(),
                pointY - eraseTool.getRadius(),
                paint);
    }

    public void setRadius(int value)
    {
        charcoalTool.setRadius(value);
        eraseTool.setRadius(value);
    }

    public void setEraseMode(boolean isOn) {
        isEraseMode = isOn;
    }
}
