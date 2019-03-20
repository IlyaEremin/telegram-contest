package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class ChartView extends View {

    private static final String TAG            = "ChartView";
    public static final  int    SEGMENTS_COUNT = 5;

    private Chart chart;
    boolean[] visible;
    private float     scaleY;
    private float     scaleX;
    private float[][] drawingPointsForChart;
    private Paint[]   columnPaints;
    private Paint     gridPaint;
    private Paint     axisLabelPaint;
    float maxColumnValue = Float.MIN_VALUE;
    float intervalLength = Float.MIN_VALUE;
    private int left  = 0;
    private int right = 100;

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(0xffEDEDEE);
        gridPaint.setStrokeWidth(2f);
        axisLabelPaint = new TextPaint();
        axisLabelPaint.setAntiAlias(true);
        axisLabelPaint.setTextSize(12 * getDensity());
        axisLabelPaint.setColor(0xFF9AA5AC);
    }

    private float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    public void setChart(@NonNull final Chart chart) {
        this.chart = chart;

        columnPaints = new Paint[chart.columns.size()];
        List<Column> columns = chart.columns;
        visible = new boolean[chart.columns.size()];
        Arrays.fill(visible, true);
        for (int i = 0; i < columns.size(); i++) {
            final Column column = columns.get(i);
            float        width  = column.getWidth();
            float        height = column.getHeight();
            if (maxColumnValue < height) {
                maxColumnValue = height;
            }
            if (intervalLength < width) {
                intervalLength = width;
            }
            columnPaints[i] = new Paint();
            columnPaints[i].setColor(column.color);
            columnPaints[i].setStrokeWidth(2 * getDensity());
        }
        post(new Runnable() {
            @Override
            public void run() {
                translateCoordinates();
            }
        });
    }

    private void translateCoordinates() {
        drawingPointsForChart = new float[chart.columns.size()][];
        scaleX = getWidth() / chart.getWidth(left, right);
        scaleY = getHeight() / maxColumnValue;
        List<Column> columns = chart.columns;
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            drawingPointsForChart[i] = translate(column.points, scaleX, scaleY);
        }
    }

    private float[] translate(float[] points, float scaleX, float scaleY) {
        float[] result = new float[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                float transition = intervalLength * left / 100;
                result[i] = (points[i] - transition) * scaleX;
            } else {
                result[i] = points[i] * scaleY;
            }
        }
        return result;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (chart == null) return;

        drawYAxisValues(canvas);
        drawColumns(canvas, chart.columns);
    }

    private void drawYAxisValues(@NonNull Canvas canvas) {
        for (int i = 0; i <= SEGMENTS_COUNT; i++) {
            float lineY = getHeight() / SEGMENTS_COUNT * i;
            canvas.drawLine(0, lineY, getWidth(), lineY, gridPaint);
            int value = (int) (maxColumnValue - (int) (maxColumnValue / SEGMENTS_COUNT * i));
            canvas.drawText(String.valueOf(value), 0, lineY - 2 * getDensity(), axisLabelPaint);
        }
    }

    private void drawColumns(Canvas canvas, List<Column> columns) {
        canvas.save();
        canvas.scale(1f, -1f, 0, getHeight() / 2);

        for (int i = 0; i < columns.size(); i++) {
            if (visible[i]) {
                canvas.drawLines(drawingPointsForChart[i], columnPaints[i]);
                canvas.drawLines(drawingPointsForChart[i], 2, drawingPointsForChart[i].length - 2, columnPaints[i]);
            }
        }

        canvas.restore();
    }

    public void setInterval(@IntRange(from = 0, to = 100) int left, @IntRange(from = 0, to = 100) int right) {
        this.left = left;
        this.right = right;
        maxColumnValue = Float.MIN_VALUE;
        for (int i = 0; i < this.chart.columns.size(); i++) {
            Column column              = this.chart.columns.get(i);
            float  maxHeightOfInterval = column.getHeighthOf(left, right);
            if (maxColumnValue < maxHeightOfInterval) {
                maxColumnValue = maxHeightOfInterval;
            }
        }
        translateCoordinates();
        invalidate();
    }
}
