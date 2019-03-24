package com.ilyaeremin.graphicmodule;

import java.util.Arrays;

class Column {
    private final String id;
    String  name;
    int     color;
    long[]  xs;
    float[] ys;
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
        if (!Arrays.equals(ys, column.ys)) return false;
        return type == column.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + color;
        result = 31 * result + Arrays.hashCode(ys);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public float getWidth() {
        return xs[xs.length - 1] - xs[0];
    }

    public float getHeight() {
        return getHeightOf(0, 100);
    }

    public float getHeightOf(float left, float right) {
        float min = 0;
        float max = Float.MIN_VALUE;
        for (int i = (int) (ys.length * left / 100); i < ys.length * right / 100; i ++) {
            if (ys[i] > max) {
                max = ys[i];
            }
            if (ys[i] < min) {
                min = ys[i];
            }
        }
        return max - min;
    }
}
