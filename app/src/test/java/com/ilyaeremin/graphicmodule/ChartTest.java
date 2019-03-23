package com.ilyaeremin.graphicmodule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ChartTest {
    private final float[] expectedPoints = new float[]{
        0, Float.MAX_VALUE, // <- [0 - 12)%
        100, Float.MAX_VALUE, // <- [12 - 22)%
        200, Float.MAX_VALUE, // <- [22 - 33)%
        300, Float.MAX_VALUE, // <- [33 - 45)%
        400, Float.MAX_VALUE, // <- [45 - 56)%
        500, Float.MAX_VALUE, // <- [56 - 67)%
        600, Float.MAX_VALUE, // <- [67 - 78)%
        700, Float.MAX_VALUE, // <- [78 - 89)%
        800, Float.MAX_VALUE, // <- [89 - 100)%
        1000, Float.MAX_VALUE // <- [100 - 100]%
    };

    private Chart chart;

    @Before
    public void setUp() {
        final Column expectedColumn = new Column("does_not_matter");
        expectedColumn.ys = expectedPoints;

        chart = new Chart();
        chart.columns = new ArrayList<Column>() {{
            add(expectedColumn);
        }};
    }

    @Test
    public void getWidthTest_fullRange() {
        // Arrange
        final float expectedWidth = 1000;

        int expectedFrom = 0;
        int expectedTo   = 100;

        // Act
        float actualWidth = chart.getWidth(expectedFrom, expectedTo);

        // Assert
        Assert.assertEquals(expectedWidth, actualWidth, 0.000001);
    }

    @Test
    public void getWidthTest_oneToNinety() {
        // Arrange
        final float expectedWidth = 800;

        int expectedFrom = 0;
        int expectedTo   = 90;

        // Act
        float actualWidth = chart.getWidth(expectedFrom, expectedTo);

        // Assert
        Assert.assertEquals(expectedWidth, actualWidth, 0.000001);
    }

    @Test
    public void getWidthTest_sameFromAndTo() {
        // Arrange
        final float expectedWidth = 0;

        int expectedFrom = 50;
        int expectedTo   = 50;

        // Act
        float actualWidth = chart.getWidth(expectedFrom, expectedTo);

        // Assert
        Assert.assertEquals(expectedWidth, actualWidth, 0.000001);
    }

    @Test
    public void getWidthTest_sameFromAndToHandred() {
        // Arrange
        final float expectedWidth = 0;

        int expectedFrom = 100;
        int expectedTo   = 100;

        // Act
        float actualWidth = chart.getWidth(expectedFrom, expectedTo);

        // Assert
        Assert.assertEquals(expectedWidth, actualWidth, 0.000001);
    }

    @Test
    public void getWidthTest_notTenDividers() {
        // Arrange
        final float expectedWidth = 200;

        int expectedFrom = 13;
        int expectedTo   = 37;

        // Act
        float actualWidth = chart.getWidth(expectedFrom, expectedTo);

        // Assert
        Assert.assertEquals(expectedWidth, actualWidth, 0.000001);
    }

    @Test
    public void getXForPercentageTest_fifty() {
        // Arrange
        final int   expectedPercentage = 50;
        final float expectedPosition   = 400;

        // Act
        float actualPosition = chart.getXForPercentage(expectedPercentage);

        // Assert
        Assert.assertEquals(expectedPosition, actualPosition, 0.00001f);
    }

    @Test
    public void getXForPercentageTest_fiftyOne() {
        // Arrange
        final int   expectedPercentage = 50;
        final float expectedPosition   = 400;

        // Act
        float actualPosition = chart.getXForPercentage(expectedPercentage);

        // Assert
        Assert.assertEquals(expectedPosition, actualPosition, 0.00001f);
    }
}