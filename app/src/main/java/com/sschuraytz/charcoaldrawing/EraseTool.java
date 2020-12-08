package com.sschuraytz.charcoaldrawing;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class EraseTool extends Tool{


    private static Paint getTransparentPaint() {
        Paint transparentPaint = new Paint();
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        return transparentPaint;
    }

    public EraseTool() {
        super(getTransparentPaint());
    }

    @Override
    protected void printTexturedCircle () {
        super.printTexturedCircle(3);
    }

}
