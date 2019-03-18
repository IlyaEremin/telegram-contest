package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class ChartView extends View {

    private static final String TAG = "ChartView";

    private Chart   chart;
    private float   scaleY;
    private float   scaleX;
    private Paint[] paints;

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChart(@NonNull Chart chart) {
        float maxColumnWidth  = -1;
        float maxColumnHeight = -1;
        paints = new Paint[chart.columns.size()];
        List<Column> columns = chart.columns;
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            float  width  = column.getWidth();
            float  height = column.getHeight();
            if (maxColumnHeight < height) {
                maxColumnHeight = height;
            }
            if (maxColumnWidth < width) {
                maxColumnWidth = width;
            }
            paints[i] = new Paint();
            paints[i].setColor(column.color);
            paints[i].setStrokeWidth(10f);
        }
        this.chart = chart;
        final float finalMaxColumnWidth  = maxColumnWidth;
        final float finalMaxColumnHeight = maxColumnHeight;
        post(new Runnable() {
            @Override
            public void run() {
                scaleX = getWidth() / finalMaxColumnWidth;
                scaleY = getHeight() / finalMaxColumnHeight;
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.scale(scaleX, scaleY);
        if (chart == null) return;

        List<Column> columns = chart.columns;
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            canvas.drawLines(column.points, paints[i]);
            // canvas.drawLines(new float[]{0f, 0f, 100f, 100f, 100f, 200f, 100f, 300f}, paints[i]);
        }
    }
}
