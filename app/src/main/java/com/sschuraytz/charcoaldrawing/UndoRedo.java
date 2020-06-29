package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Stack;

public class UndoRedo {

    private Stack<Bitmap> currentStack = new Stack<>();
    private Stack<Bitmap> undoneStack = new Stack<>();
    private Canvas bitmapCanvas;
    private UndoRedoListener undoRedoListener = new UndoRedoListener() {
        public void onUndoAvailable(boolean isAvailable) { }
        public void onRedoAvailable(boolean isAvailable) { }
    };

    public UndoRedo() {
        onSizeChanged(1, 1);
    }

    //ensure we always have a bitmap to display
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
        currentStack.push(newBitmap);
        bitmapCanvas = new Canvas(newBitmap);
        undoRedoListener.onUndoAvailable(currentStack.size() > 1);
        //after undo, if draw new line, do not let user redo
        undoneStack.clear();
        undoRedoListener.onRedoAvailable(false);
    }

    public Bitmap getCurrentBitmap() {
        return currentStack.peek();
    }

    public void undo() {
        if (currentStack.size() > 1) {
            undoneStack.push(currentStack.pop());
            bitmapCanvas = new Canvas(currentStack.peek());
            undoRedoListener.onUndoAvailable(currentStack.size() > 1);
            undoRedoListener.onRedoAvailable(undoneStack.size() > 0);
        }
    }

    public void redo() {
        if (undoneStack.size() > 0 ) {
            currentStack.push(undoneStack.pop());
            undoRedoListener.onUndoAvailable(currentStack.size() > 1);
            undoRedoListener.onRedoAvailable(undoneStack.size() > 0);
        }
    }

    public void createNewCanvas() {
        int width = getCurrentBitmap().getWidth();
        int height = getCurrentBitmap().getHeight();
        onSizeChanged(width, height);
        undoRedoListener.onUndoAvailable(false);
        undoRedoListener.onRedoAvailable(false);
    }

    public Canvas getBitmapCanvas() {
        return bitmapCanvas;
    }

    public void setListener(UndoRedoListener undoRedoListener) {
        this.undoRedoListener = undoRedoListener;
    }
}
