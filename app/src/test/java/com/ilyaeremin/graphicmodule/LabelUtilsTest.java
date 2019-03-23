package com.ilyaeremin.graphicmodule;

import org.junit.Assert;
import org.junit.Test;

public class LabelUtilsTest {

    @Test
    public void generateIntervalTest_fullInterval() {
        // Assert
        float   expectedLeftBound       = 0f;
        float   expectedRightBound      = 100f;
        int     expectedVisibleOnScreen = 4;
        float[] expectedDatesX          = {0f, 33.33333f, 66.666666f, 100f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.00001f);
    }

    @Test
    public void generateIntervalTest_middle() {
        // Assert
        float   expectedLeftBound       = 25f;
        float   expectedRightBound      = 45f;
        int     expectedVisibleOnScreen = 3;
        float[] expectedDatesX          = {20f, 30f, 40f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.00001f);
    }

    @Test
    public void generateIntervalTest_closeToLeftBound() {
        // Assert
        float   expectedLeftBound       = 0f;
        float   expectedRightBound      = 30f;
        int     expectedVisibleOnScreen = 3;
        float[] expectedDatesX          = {0f, 14.285714f, 28.571428f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.00001f);
    }

    @Test
    public void generateIntervalTest_closeToRightBound() {
        // Assert
        float   expectedLeftBound       = 50f;
        float   expectedRightBound      = 100f;
        int     expectedVisibleOnScreen = 3;
        float[] expectedDatesX          = {50f, 75f, 100f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.00001f);
    }

    @Test
    public void generateIntervalTest_73to100() {
        // Assert
        float   expectedLeftBound       = 73f;
        float   expectedRightBound      = 100f;
        int     expectedVisibleOnScreen = 5;
        float[] expectedDatesX          = {66.666666666666666f, 73.333333333333333f, 80f, 86.666666666666666f, 93.333333333333333f, 100f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.0001f);
    }

    @Test
    public void generateIntervalTest_79to100() {
        // Assert
        float   expectedLeftBound       = 79f;
        float   expectedRightBound      = 100f;
        int     expectedVisibleOnScreen = 5;
        float[] expectedDatesX          = {78.947365f, 84.210526f, 89.47368f, 94.73684f, 100f};

        // Act
        float[] actualDatesX = LabelUtils.generateDatesX(expectedLeftBound, expectedRightBound, expectedVisibleOnScreen);

        // Assert
        Assert.assertArrayEquals(expectedDatesX, actualDatesX, 0.0001f);
    }
}