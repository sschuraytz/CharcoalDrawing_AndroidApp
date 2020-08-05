package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DrawingView extends View {

    private Paint paint;
    protected UndoRedo undoRedo = new UndoRedo();


    private CharcoalTool charcoalTool;
    private EraseTool eraseTool;
    private Tool currentTool;

    private float previousX;
    private float previousY;
    private final int OPACITY_INCREMENTER = 25;

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
                // avoid continuously reprinting circle (thus making it darker) if user keeps finger on same spot
                if (pointX != previousX || pointY != previousY) {
                    currentTool.drawContinuouslyBetweenPoints(undoRedo.getBitmapCanvas(), pointX, pointY, previousX, previousY);
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

    //called on create
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        undoRedo.onSizeChanged(width, height);
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

    protected boolean undo() {
        boolean isAvailable = undoRedo.undo();
        invalidate();
        return isAvailable;
    }

    protected boolean redo() {
        boolean isAvailable = undoRedo.redo();
        invalidate();
        return isAvailable;
    }

    public void createNewCanvas() {
        undoRedo.createNewCanvas();
        initializeDrawing();
        invalidate();
    }

    public void lighter() {
        //since alpha of each tool is set using opacity & radius, ensure that their difference is less than max-alpha (255)
        if (currentTool.getOpacity() >= (currentTool.getRadius() + OPACITY_INCREMENTER)) {
            showToast("lighter");
            currentTool.updateOpacity(-OPACITY_INCREMENTER);
        }
        else {
            showToast("cannot be lighter");
        }
    }

    public void darker() {
        //since alpha of each tool is set using opacity & radius, ensure that their difference is less than max-alpha (255)
        if (currentTool.getOpacity() - currentTool.getRadius() + OPACITY_INCREMENTER <= 255) {
            showToast("darker");
            currentTool.updateOpacity(OPACITY_INCREMENTER);
        }
        else {
            showToast("cannot be darker");
        }
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void save() {

    }
}