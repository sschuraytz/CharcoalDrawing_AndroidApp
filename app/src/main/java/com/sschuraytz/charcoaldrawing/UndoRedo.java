package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Stack;

public class UndoRedo {

    private Stack<Bitmap> currentStack = new Stack<>();
    private Stack<Bitmap> undoneStack = new Stack<>();
    private Canvas bitmapCanvas;

    public UndoRedo() {
        onSizeChanged(200, 200);
    }

    //ensure we always have a bitmap to display
    protected void onSizeChanged(int width, int height) {
        currentStack.clear();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentStack.push(bitmap);
        bitmapCanvas = new Canvas(bitmap);
    }

    public void addBitmap() {
        Bitmap currentTop = currentStack.peek();
        Bitmap newBitmap = Bitmap.createBitmap(currentTop, 0, 0, currentTop.getWidth(), currentTop.getHeight());
        currentStack.push(newBitmap);
        bitmapCanvas = new Canvas(newBitmap);
    }

    public Bitmap getCurrentBitmap() {
        return currentStack.peek();
    }

    public void undo() {
        if (currentStack.size() > 1) {
            undoneStack.push(currentStack.pop());
            bitmapCanvas = new Canvas(currentStack.peek());
        }
    }

    public void redo() {
        if (undoneStack.size() > 0 ) {
            currentStack.push(undoneStack.pop());
        }
    }

    public Canvas getBitmapCanvas() {
        return bitmapCanvas;
    }

    public int getCurrentStackSize() {
        return currentStack.size();
    }
}
