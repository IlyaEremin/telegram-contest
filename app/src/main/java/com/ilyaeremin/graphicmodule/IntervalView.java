package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class IntervalView extends View {

    private static final String TAG = "IntervalView";

    private static final int MIN_INTERVAL          = 50;
    private static final int CHIN_WIDTH            = 6;
    private static final int NON_ACTIVE_AREA_COLOR = 0xCCF5F8F9;
    private static final int ACTIVE_AREA_BORDER    = 0xCCDCE7EF;

    private IntervalListener intervalListener;
    private Paint            activeAreaPaint;
    private Paint            activeAreaBorderPaint;

    private boolean expandLeftBound  = false;
    private boolean expandRightBound = false;
    private float   leftBound;
    private float   rightBound;
    private float   previousTouchX   = -1;

    public IntervalView(Context context) {
        super(context);
        init(null);
    }

    public IntervalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IntervalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        activeAreaPaint = new Paint();
        activeAreaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        activeAreaPaint.setAntiAlias(true);

        activeAreaBorderPaint = new Paint();
        activeAreaBorderPaint.setColor(ACTIVE_AREA_BORDER);
        activeAreaBorderPaint.setStyle(Paint.Style.STROKE);
        activeAreaBorderPaint.setStrokeWidth(toDp(CHIN_WIDTH));
        post(new Runnable() {
            @Override
            public void run() {
                leftBound = getWidth() * 0.8f;
                rightBound = getWidth();
                invalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.abs(touchX - getLeftBound()) < CHIN_WIDTH) {
                    expandLeftBound = true;
                } else if (Math.abs(touchX - getRightBound()) < CHIN_WIDTH) {
                    expandRightBound = true;
                } else if (touchX > leftBound && touchX < rightBound) {
                    previousTouchX = touchX;
                } else {
                    previousTouchX = touchX;
                    float interval = rightBound - leftBound;
                    setBounds(touchX - interval / 2, touchX + interval / 2);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (expandLeftBound) {
                    setBounds(touchX, this.rightBound);
                } else if (expandRightBound) {
                    setBounds(this.leftBound, touchX);
                } else if (previousTouchX > 0) {
                    float dx = touchX - previousTouchX;
                    setBounds(leftBound + dx, rightBound + dx);
                    previousTouchX = touchX;
                }
                return true;
            case MotionEvent.ACTION_UP:
                expandLeftBound = false;
                expandRightBound = false;
                previousTouchX = -1;
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void setBounds(float leftBound, float rightBound) {
        if (leftBound < 0 || rightBound > getWidth() || rightBound - leftBound < toDp(MIN_INTERVAL)) {
            return;
        }
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        notifyListener();
    }

    private void notifyListener() {
        invalidate();
        if (intervalListener != null) {
            int left  = (int) (this.leftBound * 100 / getWidth());
            int right = (int) (this.rightBound * 100 / getWidth());
            Log.i(TAG, "left: " + left + " right: " + right);
            intervalListener.onChanged(left, right);
        }
    }

    private float toDp(int value) {
        return value * getResources().getDisplayMetrics().density;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(NON_ACTIVE_AREA_COLOR);
        canvas.drawRect(getLeftBound(), 0, getRightBound(), getBottom(), activeAreaPaint);
        canvas.drawRect(getLeftBound(), 0, getRightBound(), getBottom(), activeAreaBorderPaint);

    }

    private float getLeftBound() {
        return leftBound;
    }

    private float getRightBound() {
        return rightBound;
    }

    public void setOnIntervalChangeListener(IntervalListener listener) {
        this.intervalListener = listener;
    }
}
