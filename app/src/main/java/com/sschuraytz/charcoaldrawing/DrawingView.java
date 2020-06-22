package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
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
    private SmudgeTool smudgeTool;
    private Tool currentTool;

    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        charcoalTool = new CharcoalTool();
        eraseTool = new EraseTool();
        smudgeTool = new SmudgeTool();
        initializeDrawing();
    }

    private void initializeDrawing() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //make line strokes instead of painting area
        paint.setStyle(Paint.Style.STROKE);
        currentTool = charcoalTool;
        //paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               currentTool.printToCanvas(bitmapCanvas, pointX, pointY, paint);
//                currentTool.drawContinuouslyBetweenPoints(bitmapCanvas, pointX, pointY, pointX, pointY, paint);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_MOVE:
                currentTool.drawContinuouslyBetweenPoints(bitmapCanvas, pointX, pointY, previousX, previousY, paint);
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

    @Override
    protected void onSizeChanged(int width, int height, int previousHeight, int previousWidth) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    public void setRadius(int value)
    {
        charcoalTool.setRadius(value);
        eraseTool.setRadius(value);
    }

    public void setEraseMode() {
        currentTool = eraseTool;
    }

    public void setDrawingMode() {
        currentTool = charcoalTool;
    }

    public void setSmudgeMode() {
        currentTool = smudgeTool;
    }
}
