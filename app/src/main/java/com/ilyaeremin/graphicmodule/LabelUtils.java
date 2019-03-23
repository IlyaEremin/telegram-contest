package com.ilyaeremin.graphicmodule;

public class LabelUtils {

    private static final String TAG = "LabelUtils";

    public static float[] generateDatesX(float leftBound, float rightBound, int visibleOnScreen) {
        final float selectedInterval = rightBound - leftBound;
        float       stepLength       = selectedInterval / (visibleOnScreen - 1);
        stepLength = 100f / Math.round((100 / stepLength));
        float         startPosition = (int)(leftBound / stepLength) * stepLength;
        float         endPosition   = (int)(rightBound / stepLength) * stepLength;
        final float[] datesX        = new float[(int) ((endPosition - startPosition) / stepLength + 1)];
        float         position      = startPosition;
        int           i             = 0;

        while (i < datesX.length) {
            datesX[i] = position;
            position += stepLength;
            i++;
        }
        return datesX;
    }
}
