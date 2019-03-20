package com.ilyaeremin.graphicmodule;

import java.util.Arrays;

class Column {
    private final String id;
    String  name;
    int     color;
    float[] points;
    Type    type;

    Column(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (color != column.color) return false;
        if (!id.equals(column.id)) return false;
        if (!name.equals(column.name)) return false;
        if (!Arrays.equals(points, column.points)) return false;
        return type == column.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + color;
        result = 31 * result + Arrays.hashCode(points);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public float getWidth() {
        return points[points.length - 2] - points[0];
    }

    public float getWidthOf(int left, int right) {
        return points[points.length / 100 * right] - points[points.length / 100 * left];
    }

    public float getHeight() {
        return getHeighthOf(0, 100);
    }

    public float getHeighthOf(int left, int right) {
        float min = 0;
        float max = Float.MIN_VALUE;
        for (int i = 1 + MathUtils.roundEven(points.length * left / 100); i < points.length * right / 100; i += 2) {
            if (points[i] > max) {
                max = points[i];
            }
            if (points[i] < min) {
                min = points[i];
            }
        }
        return max - min;
    }
}
