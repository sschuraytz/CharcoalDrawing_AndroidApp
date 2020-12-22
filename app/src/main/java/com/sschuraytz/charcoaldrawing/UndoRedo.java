package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Stack;

public class UndoRedo {

    protected Stack<Bitmap> currentStack = new Stack<>();
    private Stack<Bitmap> undoneStack = new Stack<>();
    private Canvas bitmapCanvas;
    private final int MAX_NUM_OF_BITMAPS = 25;

    public UndoRedo() {
        onSizeChanged(1, 1);
    }

    //ensure we always have a bitmap to display to avoid java.util.EmptyStackException when getCurrentBitmap
    protected void onSizeChanged(int width, int height) {
        currentStack.clear();
        undoneStack.clear();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentStack.push(bitmap);
        bitmapCanvas = new Canvas(bitmap);
    }

    public void addBitmap() {
        Bitmap currentTop = currentStack.peek();
        Bitmap newBitmap = Bitmap.createBitmap(currentTop, 0, 0, currentTop.getWidth(), currentTop.getHeight());
        // to avoid java.lang.OutOfMemoryError
        if (currentStack.size() == MAX_NUM_OF_BITMAPS) {
            currentStack.remove(0).recycle();
        }
        currentStack.push(newBitmap);
        bitmapCanvas = new Canvas(newBitmap);
        //after undo, if draw new line, do not let user redo
        undoneStack.clear();
        bitmapCanvas.drawColor(Color.TRANSPARENT);
    }

    public Bitmap getCurrentBitmap() {
        return currentStack.peek();
    }

    public boolean undo() {
        if (currentStack.size() > 1) {
            undoneStack.push(currentStack.pop());
            bitmapCanvas = new Canvas(currentStack.peek());
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (undoneStack.size() > 0 ) {
            currentStack.push(undoneStack.pop());
            return true;
        }
        return false;
    }

    public void createNewCanvas() {
        int width = getCurrentBitmap().getWidth();
        int height = getCurrentBitmap().getHeight();
        onSizeChanged(width, height);
    }

    public Canvas getBitmapCanvas() {
        return bitmapCanvas;
    }

}
