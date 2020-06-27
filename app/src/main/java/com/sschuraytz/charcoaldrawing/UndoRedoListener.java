package com.sschuraytz.charcoaldrawing;

public interface UndoRedoListener {

    void onUndoAvailable(boolean isAvailable);
    void onRedoAvailable(boolean isAvailable);

}
