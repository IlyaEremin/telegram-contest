package com.ilyaeremin.graphicmodule;

import java.util.ArrayList;
import java.util.List;

class Column {
    private final String id;
    String      name;
    int         color;
    List<Point> points;
    Type        type;

    Column(String id) {
        this.id = id;
        this.points = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (color != column.color) return false;
        if (!id.equals(column.id)) return false;
        if (!name.equals(column.name)) return false;
        if (!points.equals(column.points)) return false;
        return type == column.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + color;
        result = 31 * result + points.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
