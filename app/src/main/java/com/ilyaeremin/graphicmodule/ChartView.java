package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
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
    private float[][] animatationPositions;
    private float[][] movingDirections;
    private Paint[]   columnPaints;
    private Paint     gridPaint;
    private Paint     axisLabelPaint;
    float maxColumnValue = Float.MIN_VALUE;
    float intervalLength = Float.MIN_VALUE;
    private int     left                = 0;
    private int     right               = 100;
    private boolean enableGrid;
    private boolean enableLabels;
    private int     animationFramesLeft = 0;

    public ChartView(Context context) {
        super(context);
        init(null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0);
        try {
            this.enableGrid = a.getBoolean(R.styleable.ChartView_enableGrid, false);
            this.enableLabels = a.getBoolean(R.styleable.ChartView_enableLabels, false);
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
        animatationPositions = new float[columnsCount][];
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
                float maxHeightOfInterval = column.getHeighthOf(left, right);
                if (maxColumnValue < maxHeightOfInterval) {
                    maxColumnValue = maxHeightOfInterval;
                }
            }
        }
        float        scaleX  = getChartCanvasWidth() / chart.getWidth(left, right);
        float        scaleY  = getChartCanvasHeight() / maxColumnValue;
        List<Column> columns = chart.columns;
        for (int i = 0; i < columns.size(); i++) {
            Column  column = columns.get(i);
            float[] points = translate(column.points, chartPoints[i], scaleX, scaleY);
            if (chartPoints[i] == null) {
                animatationPositions[i] = Arrays.copyOf(points, points.length);
                movingDirections[i] = new float[points.length];
            } else {
                calculateMovingVector();
            }
            chartPoints[i] = points;
        }
        invalidate();
    }

    private void calculateMovingVector() {
        for (int i = 0; i < chartPoints.length; i++) {
            float[] chartPoint = chartPoints[i];
            for (int j = 0; j < chartPoint.length; j++) {
                float point = chartPoint[j];
                movingDirections[i][j] = (point - animatationPositions[i][j]) / FRAMES_PER_ANIMATION;
            }
        }
        animationFramesLeft = FRAMES_PER_ANIMATION - 1;
    }

    private boolean isColumnVisible(int i) {
        return columnsVisibility[i];
    }

    private float[] translate(float[] points, float[] currentPoints, float scaleX, float scaleY) {
        float[] result;
        if (currentPoints == null) {
            result = new float[points.length];
        } else {
            result = currentPoints;
        }
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                float transition = intervalLength * left / 100;
                result[i] = (points[i] - points[0] - transition) * scaleX;
            } else {
                result[i] = points[i] * scaleY + chartBottomPadding();
            }
        }
        return result;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (chart == null) return;

        if (this.enableGrid) {
            drawGrid(canvas);
        }
        drawColumns(canvas, chart.columns);
        if (this.enableLabels) {
            drawLabels(canvas);
        }
    }

    private void drawLabels(@NonNull final Canvas canvas) {
        for (int i = 0; i <= SEGMENTS_COUNT; i++) {
            float lineY = getChartCanvasHeight() / SEGMENTS_COUNT * i;
            int   value = (int) (maxColumnValue - (int) (maxColumnValue / SEGMENTS_COUNT * i));
            canvas.drawText(String.valueOf(value), 0, lineY - toDp(Y_LABELS_BOTTOM_PADDING), axisLabelPaint);
        }
        long from     = (long) chart.getXForPercentage(left);
        long to       = (long) chart.getXForPercentage(right);
        long interval = (to - from) / (DATES_COUNT - 1);

        float lastLabelWidth = axisLabelPaint.measureText(DateFormatter.format(to));
        for (int i = 0; i < DATES_COUNT; i++) {
            int    dateX = (int) ((getChartCanvasWidth() - lastLabelWidth) * i / (DATES_COUNT - 1));
            float  dateY = getHeight();
            String date  = DateFormatter.format(from + interval * i);
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
        return axisLabelPaint.getTextSize() + toDp(X_LABELS_TOP_PADDING);
    }

    private float getChartCanvasWidth() {
        return getWidth();
    }

    private void drawColumns(Canvas canvas, List<Column> columns) {
        canvas.save();
        mirrorVertically(canvas);
        boolean continueAnimation = false;
        for (int i = 0; i < columns.size(); i++) {
            if (isColumnVisible(i) && chartPoints[i] != null) {
                animateColumn(i);
                canvas.drawLines(animatationPositions[i], columnPaints[i]);
                canvas.drawLines(animatationPositions[i], 2, animatationPositions[i].length - 2, columnPaints[i]);
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
            animatationPositions[columnNumber][i] += movingDirections[columnNumber][i];
        }
    }

    private void mirrorVertically(Canvas canvas) {
        canvas.scale(1f, -1f, 0, getHeight() / 2);
    }

    public void setLeftBound(@IntRange(from = 0, to = 100) int leftBound) {
        setInterval(leftBound, this.right);
    }

    public void setRightBound(int rightBound) {
        setInterval(this.left, rightBound);
    }

    public void setInterval(@IntRange(from = 0, to = 100) int left, @IntRange(from = 0, to = 100) int right) {
        this.left = left;
        this.right = right;
        calculateCoordinates();
    }

    public void setColumnVisibility(int position, boolean visible) {
        this.columnsVisibility[position] = visible;
        calculateCoordinates();
    }
}
