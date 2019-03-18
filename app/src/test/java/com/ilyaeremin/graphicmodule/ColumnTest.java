package com.ilyaeremin.graphicmodule;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColumnTest {

    @Test
    public void getWidthTest() {
        // Assert
        final String expectedId    = "y1";
        final Column column        = new Column(expectedId);
        final long   expectedWidth = 100L;
        column.points = new float[]{0L, 0L, 100L, 0L};

        // Act
        float actualWidth = column.getWidth();

        // Assert
        assertEquals(expectedWidth, actualWidth, 0.000001f);
    }

    @Test
    public void getHeightTest() {
        // Assert
        final String expectedId     = "y1";
        final Column column         = new Column(expectedId);
        final float  expectedHeight = 123L;
        column.points = new float[]{0, 0, 0, 123};

        // Act
        float actualHeight = column.getHeight();

        // Assert
        assertEquals(expectedHeight, actualHeight, 0.000001f);
    }
}