package com.sschuraytz.charcoaldrawing;

public interface VoiceListener {

    void updateFABUI();
    void charcoalCommand();
    void eraserCommand();
    boolean undoCommand();
    boolean redoCommand();
    void createNewCanvasCommand();
    void updateDrawingThickness(int radius);
    void saveDrawing();
    void help();
    void lighter();
    void darker();

}
