package com.ilyaeremin.graphicmodule.utils;

public class MathUtils {
    public static int roundEven(int number) {
        return Math.round(number >> 1) * 2;
    }
}
