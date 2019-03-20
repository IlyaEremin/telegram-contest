package com.ilyaeremin.graphicmodule.utils;

public class ScaleUtils {

    public static float calculateScale(float inputWidth, float inputHeight, float viewPortWidth, float viewPortHeight) {
        float widthScale  = viewPortWidth / inputWidth;
        float heightScale = viewPortHeight / inputHeight;
        return widthScale < heightScale ? widthScale : heightScale;
    }
}
