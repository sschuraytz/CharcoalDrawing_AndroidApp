package com.sschuraytz.charcoaldrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class EraseTool extends Tool{

    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private final Paint paintDstIn = new Paint();


    public EraseTool() {
        //the color set here doesn't matter because color is not used when drawing a bitmap, paint options are - see paint setting below
        super(Color.BLACK);
        //PorterDuff Destination (DST) is what already exists on the canvas. Source (SRC) is what's drawn on top of DST
        //DST_OUT results in transparent canvas anywhere that SRC overlaps with DST
        paintDstIn.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void createNewBuffers() {
        bufferBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);
    }

    @Override
    protected void drawSinglePoint(Canvas bitmapCanvas, float x, float y) {
        //recreate the buffers so that when adjust size of erase tool and need to redraw, it won't end up with multiple circles
        createNewBuffers();
        bufferCanvas.drawBitmap(this.getBitmap(), 0, 0, null);
        bitmapCanvas.drawBitmap(bufferBitmap, x, y, paintDstIn);
        //TODO: fix this so it draws circle center where finger is pressed, as opposed to upper-left where finger is pressed
    }

    @Override
    //eraser has higher density than charcoal because it's less textured
    protected void printTexturedCircle () {
        super.printTexturedCircle(3);
    }

}
