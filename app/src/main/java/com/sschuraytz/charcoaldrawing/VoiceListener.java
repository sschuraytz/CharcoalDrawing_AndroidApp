package com.sschuraytz.charcoaldrawing;

public interface VoiceListener {

    void updateFABUI();
    void charcoalCommand();
    void eraserCommand();
    void undoCommand();
    void redoCommand();
    void createNewCanvasCommand();
    void updateDrawingThickness(int radius);

}
