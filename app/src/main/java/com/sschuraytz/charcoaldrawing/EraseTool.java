package com.sschuraytz.charcoaldrawing;

import android.graphics.Color;


public class EraseTool extends Tool{

    public EraseTool() {
        super(Color.argb(0, 250,250, 250));
    }

    @Override
    protected void printTexturedCircle () {
        super.printTexturedCircle(3);
    }

}
