package com.sschuraytz.charcoaldrawing;


public class EraseTool extends Tool{

    public EraseTool() {
        super(0xFAFAFA);
    }

    @Override
    protected void printTexturedCircle () {
        super.printTexturedCircle(3);
    }

}
