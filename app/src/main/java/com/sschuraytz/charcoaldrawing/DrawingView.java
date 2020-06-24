package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.util.Stack;

public class DrawingView extends View {

    private Paint paint;
    protected UndoRedo undoRedo = new UndoRedo();

    private float previousX;
    private float previousY;
    private CharcoalTool charcoalTool;
    private EraseTool eraseTool;
    private Tool currentTool;

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
        currentTool = charcoalTool;
        //paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                undoRedo.addBitmap();
                currentTool.drawContinuouslyBetweenPoints(undoRedo.getBitmapCanvas(), pointX, pointY, pointX, pointY);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_MOVE:
                currentTool.drawContinuouslyBetweenPoints(undoRedo.getBitmapCanvas(), pointX, pointY, previousX, previousY);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        undoRedo.onSizeChanged(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(undoRedo.getCurrentBitmap(), 0, 0, paint);
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

    protected void undo() {
        undoRedo.undo();
        invalidate();
    }
}