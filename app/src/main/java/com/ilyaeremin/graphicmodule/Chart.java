package com.ilyaeremin.graphicmodule;

import com.ilyaeremin.graphicmodule.utils.MathUtils;

import java.util.List;

public class Chart {
    private static final String TAG = "Chart";

    List<Column> columns;

    public float getWidth(int from, int to) {
        float fromX = getXForPercentage(from);
        float toX   = getXForPercentage(to);
        return toX - fromX;
    }

    public float getXForPercentage(int percentage) {
        Column column = columns.get(0);
        return column.points[MathUtils.roundEven((column.points.length - 2) * percentage / 100)];
    }
}
