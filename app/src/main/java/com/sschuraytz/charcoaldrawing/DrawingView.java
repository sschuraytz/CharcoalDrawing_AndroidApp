package com.sschuraytz.charcoaldrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class DrawingView extends View {

    //drawing primitive
    private Path path;
    //drawing style
    private Paint paint;
    //what to draw (writing into bitmap)
    private Canvas bitmapCanvas;
    //hold pixels where canvas will be drawn
    private Bitmap bitmap;
    private List<PointF> points = new ArrayList<>();
    public int radius;

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
                //points.add(new PointF(pointX, pointY));
                //addSurroundingLines(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                //points.add(new PointF(pointX, pointY));
                //path.lineTo(pointX, pointY);
                addSurroundingLines(pointX, pointY);

                break;
            case MotionEvent.ACTION_UP:
                //points.add(new PointF(pointX, pointY));
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

    @Override
    protected void onSizeChanged(int height, int width, int previousHeight, int previousWidth) {
        bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
        //cud potentially turn this loop into one line - just need list in array
/*        for (PointF point : points)
        {
            canvas.drawCircle(point.x, point.y, radius, paint);
        }*/
        //this is the place to experiment with different charcoal textures, I think
    }

    public Paint getPaint() {
        return paint;
    }

/*    public void setRadius(int value) {
        radius = value;
    }*/

    public void addSurroundingLines(float pointX, float pointY)
    {
        //path.lineTo(pointX + 2, pointY + 2);
      //  path.quadTo(pointX + 2, pointY + 2, pointX - 3, pointY - 3);
      //  path.quadTo(pointX + -2, pointY, pointX, pointY);
        //path.addRect(pointX, pointY, pointX + 1, pointY + 1, Path.Direction.CW);
        //path.addRect(pointX - 1, pointY - 1, pointX, pointY, Path.Direction.CW);
        path.addRoundRect(pointX - 14, pointY + 8,
                pointX - 14, pointY + 12, 10, 15, Path.Direction.CW);
        path.addRoundRect(pointX, pointY + 2,
                pointX, pointY, 1, 1, Path.Direction.CW);
        path.addRoundRect(pointX + 1, pointY + 5,
                pointX + 5, pointY + 5, 1, 1, Path.Direction.CW);
        path.quadTo(pointX + 1, pointY + 5, pointX + 1, pointY + 6);
        RectF rectangle = new RectF(pointX, pointX, pointY - 1, pointY - 1);
        path.addArc(rectangle, 0, -50);
    }
}
