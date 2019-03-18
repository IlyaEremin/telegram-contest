package com.ilyaeremin.graphicmodule;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScaleUtilsTest {

    @Test
    public void calculateScaleTest() {
        // Arrange
        long  expectedInputWidth  = 5000L;
        long  expectedInputHeight = 1000L;
        long  expectedViewWidth   = 1000L;
        long  expectedViewHeight  = 500L;
        float expectedScale       = 0.2f;

        // Act
        float actualScale = ScaleUtils.calculateScale(expectedInputWidth, expectedInputHeight, expectedViewWidth, expectedViewHeight);

        // Assert
        assertEquals(expectedScale, actualScale, 0.0000001);
    }

    @Test
    public void calculateScaleTest_heightHasLessScale() {
        // Arrange
        long  expectedInputWidth  = 5000L;
        long  expectedInputHeight = 1000L;
        long  expectedViewWidth   = 1000L;
        long  expectedViewHeight  = 100L;
        float expectedScale       = 0.1f;

        // Act
        float actualScale = ScaleUtils.calculateScale(expectedInputWidth, expectedInputHeight, expectedViewWidth, expectedViewHeight);

        // Assert
        assertEquals(expectedScale, actualScale, 0.0000001);
    }
}