package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class DrawingView extends View {

    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;

    private Stack<Bitmap> currentStack = new Stack<>();
    private Stack<Bitmap> undoneStack = new Stack<>();

    private float previousX;
    private float previousY;
    private CharcoalTool charcoalTool;
    private EraseTool eraseTool;
    private Tool currentTool;
    private boolean firstDraw = true;

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
        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        //paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentTool.drawContinuouslyBetweenPoints(bitmapCanvas, pointX, pointY, pointX, pointY);
                previousX = pointX;
                previousY = pointY;
                //currentStack.push(bitmap);
                break;
            case MotionEvent.ACTION_MOVE:
                currentTool.drawContinuouslyBetweenPoints(bitmapCanvas, pointX, pointY, previousX, previousY);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_UP:
                //undoStack.push(bitmap);
                currentStack.push(bitmap);
                firstDraw = false;      //don't want to check this on every touch event. is there another place to put it?
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
        if (!currentStack.empty()) {
            Bitmap top = currentStack.peek();
            canvas.drawBitmap(top, 0, 0, paint);
        }
        else if (firstDraw) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
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
        if (!currentStack.empty()) {
            undoneStack.push(currentStack.pop());
            invalidate();
        }
    }

    protected void redo() {
        if (!undoneStack.empty()) {
            currentStack.push(undoneStack.pop());
            invalidate();
        }
    }
}