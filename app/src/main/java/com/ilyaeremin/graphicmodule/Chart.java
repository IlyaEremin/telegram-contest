package com.ilyaeremin.graphicmodule;

import com.ilyaeremin.graphicmodule.utils.MathUtils;

import java.util.List;

public class Chart {
    List<Column> columns;

    public float getWidth(float from, float to) {
        float fromX = getXForPercentage(from);
        float toX   = getXForPercentage(to);
        return toX - fromX;
    }

    public float getXForPercentage(float percentage) {
        Column column = columns.get(0);
        int    index  = MathUtils.roundEven((column.xs.length - 2) * percentage / 100);
        return column.xs[index];
    }
}
