package com.example.mitch.androidpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF0000FF;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase = false, rectangle = false, circle = false;
    private Rect rect;
    private int x, y, radius;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        rect = new Rect();
    }

    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void setRectangle(boolean isRectangle) {
        rectangle = isRectangle;
        circle = false;
        erase = false;
    }

    public void setCircle(boolean isCircle) {
        circle = isCircle;
        rectangle = false;
        erase = false;
    }

    public void setBrushSize(float newSize){
        rectangle = false;
        circle = false;
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
        if (rectangle) {
            canvas.drawRect(rect, drawPaint);
        } else if (circle) {
            canvas.drawCircle(x, y, radius, drawPaint);
        } else {
            canvas.drawPath(drawPath, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectangle) {
                rect.top = (int)touchY;
                rect.right = (int)touchX;

                } else if (circle) {
                    y = (int)touchY;
                    x = (int)touchX;
                } else {
                drawPath.moveTo(touchX, touchY);

                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectangle) {
                rect.left = (int)touchX;
                rect.bottom = (int)touchY;
                } else if (circle) {
                    radius = (int)touchX - x;
                } else {
                drawPath.lineTo(touchX, touchY);

                }
                break;
            case MotionEvent.ACTION_UP:
                if (rectangle) {
                drawCanvas.drawRect(rect, drawPaint);
                } else if (circle) {
                    drawCanvas.drawCircle(x, y, radius, drawPaint);

                } else {
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();

                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }



}

