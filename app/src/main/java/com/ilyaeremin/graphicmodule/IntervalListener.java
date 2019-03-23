package com.ilyaeremin.graphicmodule;

import android.support.annotation.IntRange;

public interface IntervalListener {
    void onChanged(@IntRange(from = 0, to = 100) int leftBound, @IntRange(from = 0, to = 100) int rightBound);
}
