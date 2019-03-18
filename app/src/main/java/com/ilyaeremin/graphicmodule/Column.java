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

    public float getHeight() {
        return points[points.length - 1] - points[1];
    }
}
