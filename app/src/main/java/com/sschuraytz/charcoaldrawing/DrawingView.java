package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawingView extends View {

    //drawing primitive
    private Path path;
    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;
    private float previousX;
    private float previousY;
    private PathMeasure measure;
    private float[] pos, tan;
    private float speed, distance;


    //AttributeSet = XML attributes, need since inflating from XML
    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        initializeDrawing();
    }

    private void initializeDrawing() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //make line strokes instead of painting area
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                path.addCircle(pointX, pointY, 5, Path.Direction.CCW);
                previousX = pointX;
                previousY = pointY;
                break;
            case MotionEvent.ACTION_MOVE:
                path.addCircle(pointX, pointY, 5, Path.Direction.CCW);
               // path.lineTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
               // path.lineTo(previousX, previousY);
               // path.addCircle(pointX, pointY, 5, Path.Direction.CCW);
                drawBetween(pointX, pointY);
               // bitmapCanvas.draw(previousX, previousY, pointX, pointY, paint);
/*                measure = new PathMeasure(path, false);
                speed = measure.getLength() / 30;
                pos = new float[2];
                tan = new float[2];*/

                bitmapCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }
        //a change invalidated view layout --> onDraw method executes
        invalidate();
        return true;
    }

    private void drawBetween(float pointX, float pointY)
    {
        float smallerX = Math.min(previousX, pointX);
        float largerX = Math.max(previousX, pointX);
        float smallerY = Math.min(previousY, pointY);
        float largerY = Math.max(previousY, pointY);

        //float dx = largerX - smallerX;
        //float dy = largerY - smallerY;
        float dy = pointY - previousY;
        float dx = pointX - previousX;
        float magnitude = (float)Math.sqrt(dx * dx + dy * dy);
        float slope = dy/dx;


        for (float ix = pointX; ix < previousX; ix++)
        {
            float iy = pointY;
            path.addCircle(ix, iy, 1, Path.Direction.CCW);
            iy += slope;
        }


        if (dx > dy)
        {
            float dxIncrements = dx/dy;
            for (float iy = smallerY; iy < largerY; iy++)
            {
                path.addCircle(smallerX, iy, 1, Path.Direction.CCW);
                if (smallerX < largerX)
                {
                    smallerX += dxIncrements;
                }
            }
        }
        else if (dy > dx)
        {
            float dyIncrements = dy/dx;
            for (float ix = smallerX; ix < largerX; ix++)
            {
                path.addCircle(ix, smallerY, 1, Path.Direction.CCW);
                if (smallerY <  largerY)
                {
                    smallerY += dyIncrements;
                }
            }
        }
    }

    private float useEquation(float xVal, float currX, float yVal, float slope)
    {
        return slope * (currX - xVal) + yVal;
    }

    @Override
    protected void onSizeChanged(int height, int width, int previousHeight, int previousWidth) {
        bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
        //canvas.drawLine(previousX, previousY, pointX, pointY, paint);
        //this is the place to experiment with different charcoal textures, I think
    }

    public Paint getPaint() {
        return paint;
    }
}
