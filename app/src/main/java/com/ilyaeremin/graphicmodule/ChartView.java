package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class ChartView extends View {

    private static final String TAG                     = "ChartView";
    public static final  int    SEGMENTS_COUNT          = 5;
    public static final  int    DATES_COUNT             = 6;
    public static final  int    CHART_WIDTH             = 2;
    public static final  int    LABEL_TEXT_SIZE         = 12;
    public static final  int    Y_LABELS_BOTTOM_PADDING = 4;
    public static final  int    X_LABELS_TOP_PADDING    = 4;
    public static final  int    FRAMES_PER_ANIMATION    = 6;

    private Chart chart;
    boolean[] columnsVisibility;
    private float[][] chartPoints;
    private float[][] animationPositions;
    private float[][] movingDirections;
    private Paint[]   columnPaints;
    private Paint     gridPaint;
    private Paint     axisLabelPaint;
    private Paint     selectionPaint;
    float maxColumnValue = Float.MIN_VALUE;
    long  intervalLength = Long.MIN_VALUE;
    private float   left                = 0f;
    private float   right               = 100f;
    private boolean enableGrid;
    private boolean enableLabels;
    private int     animationFramesLeft = 0;
    private float   viewPortWidth       = 0;

    private float   scaleX;
    private boolean isPreview;
    private int     pressedX;

    public ChartView(Context context) {
        super(context);
        init(null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        gridPaint = new Paint();
        gridPaint.setColor(0xffEDEDEE);
        gridPaint.setStrokeWidth(2f);
        axisLabelPaint = new TextPaint();
        axisLabelPaint.setAntiAlias(true);
        axisLabelPaint.setTextSize(toSp(LABEL_TEXT_SIZE));
        axisLabelPaint.setColor(0xFF9AA5AC);

        selectionPaint = new Paint();
        selectionPaint.setColor(0xFFE5EBEF);
        selectionPaint.setStrokeWidth(toDp(2));

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0);
        try {
            this.enableGrid = a.getBoolean(R.styleable.ChartView_enableGrid, false);
            this.enableLabels = a.getBoolean(R.styleable.ChartView_enableLabels, false);
            this.isPreview = a.getBoolean(R.styleable.ChartView_isPreview, false);
        } finally {
            a.recycle();
        }
    }

    private float toSp(int textSize) {
        return textSize * getResources().getDisplayMetrics().scaledDensity;
    }

    private float toDp(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    public void setChart(@NonNull final Chart chart) {
        this.chart = chart;
        int columnsCount = chart.columns.size();
        columnPaints = new Paint[columnsCount];
        columnsVisibility = new boolean[columnsCount];
        chartPoints = new float[columnsCount][];
        animationPositions = new float[columnsCount][];
        movingDirections = new float[columnsCount][];

        Arrays.fill(columnsVisibility, true);
        intervalLength = chart.getWidth(left, right);
        for (int i = 0; i < columnsCount; i++) {
            final Column column = chart.columns.get(i);
            columnPaints[i] = new Paint();
            columnPaints[i].setColor(column.color);
            columnPaints[i].setStrokeWidth(toDp(CHART_WIDTH));
        }
        post(new Runnable() {
            @Override
            public void run() {
                calculateCoordinates();
            }
        });
    }

    private void calculateCoordinates() {
        maxColumnValue = Float.MIN_VALUE;
        for (int i = 0; i < this.chart.columns.size(); i++) {
            Column column = this.chart.columns.get(i);
            if (isColumnVisible(i)) {
                float maxHeightOfInterval = column.getHeightOf(left, right);
                if (maxColumnValue < maxHeightOfInterval) {
                    maxColumnValue = maxHeightOfInterval;
                }
            }
        }
        updateScaleX();
        float        scaleY  = getChartCanvasHeight() / maxColumnValue;
        List<Column> columns = chart.columns;
        for (int i = 0; i < columns.size(); i++) {
            Column  column = columns.get(i);
            float[] points = translate(column.xs, column.ys, chartPoints[i], scaleX, scaleY);
            if (chartPoints[i] == null) {
                animationPositions[i] = Arrays.copyOf(points, points.length);
                movingDirections[i] = new float[points.length];
            } else {
                calculateMovingVector();
            }
            chartPoints[i] = points;
        }
        invalidate();
    }

    private void updateScaleX() {
        Log.i(TAG, "updateScaleX: diff: " + Math.abs((right - left) - viewPortWidth));
        if (viewPortWidth == 0 || Math.abs((right - left) - viewPortWidth) > 0.05) {
            float chartCanvasWidth = getChartCanvasWidth();
            if (chartCanvasWidth > 0) {
                viewPortWidth = right - left;
                long chartWidth = chart.getWidth(left, right);
                scaleX = chartCanvasWidth / chartWidth;
            }
        }
    }

    private void calculateMovingVector() {
        for (int i = 0; i < chartPoints.length; i++) {
            float[] chartPoint = chartPoints[i];
            for (int j = 0; j < chartPoint.length; j++) {
                movingDirections[i][j] = (chartPoint[j] - animationPositions[i][j]) / FRAMES_PER_ANIMATION;
            }
        }
        animationFramesLeft = FRAMES_PER_ANIMATION - 1;
    }

    private boolean isColumnVisible(int i) {
        return columnsVisibility[i];
    }

    private float[] translate(long[] pointsX, float[] pointsY, float[] currentPoints, float scaleX, float scaleY) {
        float[] result;
        if (currentPoints == null) {
            result = new float[pointsX.length * 2];
        } else {
            result = currentPoints;
        }
        for (int i = 0; i < result.length; i++) {
            if (i % 2 == 0) {
                result[i] = (pointsX[i / 2] - getXTransition()) * scaleX;
            } else {
                result[i] = pointsY[i / 2] * scaleY + chartBottomPadding();
            }
        }
        return result;
    }

    private long getXTransition() {
        return (long) (getGraphFirstX() + intervalLength * left / 100);
    }

    private long getGraphFirstX() {
        return chart.columns.get(0).xs[0];
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (chart == null) return;

        if (this.enableGrid) {
            drawGrid(canvas);
        }
        drawColumns(canvas, chart.columns);
        drawPressedPosition(canvas);
        if (this.enableLabels) {
            drawValues(canvas);
            drawDates(canvas);
        }
    }

    private void drawPressedPosition(@NonNull Canvas canvas) {
        canvas.drawLine(pressedX, 0, pressedX, getChartCanvasHeight(), selectionPaint);
    }

    private void drawValues(@NonNull Canvas canvas) {
        axisLabelPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i <= SEGMENTS_COUNT; i++) {
            float lineY = getChartCanvasHeight() / SEGMENTS_COUNT * i;
            int   value = (int) (maxColumnValue - (int) (maxColumnValue / SEGMENTS_COUNT * i));
            canvas.drawText(String.valueOf(value), 0, lineY - toDp(Y_LABELS_BOTTOM_PADDING), axisLabelPaint);
        }
    }

    private void drawDates(@NonNull Canvas canvas) {
        axisLabelPaint.setTextAlign(Paint.Align.CENTER);
        long  transition = getXTransition();
        float dateY      = getHeight();
        Log.i(TAG, "drawDates: left: " + left + " right: " + right);
        final float[] datesX = LabelUtils.generateDatesX(left, right, DATES_COUNT);
        for (float aDatesX : datesX) {
            if (aDatesX > 100f) {
                Log.i(TAG, "drawDates: breakpoint here");
            }
            long   xForStep = chart.getXForPercentage(aDatesX);
            float  dateX    = (xForStep - transition) * scaleX;
            String date     = DateFormatter.format(xForStep);
            Log.i(TAG, "drawDates: left: " + left + " right: " + right);
            canvas.drawText(date, dateX, dateY, axisLabelPaint);
        }
    }

    private void drawGrid(@NonNull final Canvas canvas) {
        for (int i = 0; i <= SEGMENTS_COUNT; i++) {
            float lineY = getChartCanvasHeight() / SEGMENTS_COUNT * i;
            canvas.drawLine(0, lineY, getChartCanvasWidth(), lineY, gridPaint);
        }
    }

    private float getChartCanvasHeight() {
        return getHeight() - chartBottomPadding();
    }

    private float chartBottomPadding() {
        return enableGrid ? (axisLabelPaint.getTextSize() + toDp(X_LABELS_TOP_PADDING)) : 0;
    }

    private float getChartCanvasWidth() {
        return getWidth();
    }

    private void drawColumns(Canvas canvas, List<Column> columns) {
        canvas.save();
        mirrorVertically(canvas);
        for (int i = 0; i < columns.size(); i++) {
            if (isColumnVisible(i) && chartPoints[i] != null) {
                animateColumn(i);
                canvas.drawLines(animationPositions[i], columnPaints[i]);
                canvas.drawLines(animationPositions[i], 2, animationPositions[i].length - 2, columnPaints[i]);
            }
        }
        if (animationFramesLeft > 0) {
            animationFramesLeft--;
            invalidate();
        }
        canvas.restore();
    }

    private void animateColumn(int columnNumber) {
        float[] columnPoints = chartPoints[columnNumber];
        for (int i = 0; i < columnPoints.length; i++) {
            animationPositions[columnNumber][i] += movingDirections[columnNumber][i];
        }
    }

    private void mirrorVertically(Canvas canvas) {
        canvas.scale(1f, -1f, 0, getHeight() / 2);
    }

    public void setInterval(@FloatRange(from = 0, to = 100) float left, @FloatRange(from = 0, to = 100) float right) {
        this.left = left;
        this.right = right;
        calculateCoordinates();
    }

    public void setColumnVisibility(int position, boolean visible) {
        this.columnsVisibility[position] = visible;
        calculateCoordinates();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPreview) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    onPressedOnChart((int) event.getX());
                    return true;
                case MotionEvent.ACTION_UP:
                    onPressedOnChart((int) event.getX());
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void onPressedOnChart(int x) {
        pressedX = x;
        invalidate();
    }
}
