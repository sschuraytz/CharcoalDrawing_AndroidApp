package com.sschuraytz.charcoaldrawing;

import android.graphics.Color;


public class EraseTool extends Tool{

    public EraseTool() {
        super(0xFAFAFA);
    }

    @Override
    protected void printTexturedCircle () {
        super.printTexturedCircle(3);
    }

}
