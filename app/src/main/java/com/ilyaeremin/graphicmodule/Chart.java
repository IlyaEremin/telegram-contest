package com.ilyaeremin.graphicmodule;

import android.util.Log;

import java.util.List;

public class Chart {
    private static final String TAG = "Chart";
    List<Column> columns;

    public long getWidth(float from, float to) {
        Log.i(TAG, "RASSLEDOVANIE getWidth: from: " + from + " to " + to + " diff: " + (to - from));
        long fromX = getXForPercentage(from);
        long toX = getXForPercentage(to);
        return toX - fromX;
    }

    public long getXForPercentage(float percentage) {
        Column column = columns.get(0);
        int    index  = (column.xs.length - 1) * (int) percentage / 100;
        Log.i(TAG, "RASSLEDOVANIE getXForPercentage: index: " + index);
        return column.xs[index];
    }
}
