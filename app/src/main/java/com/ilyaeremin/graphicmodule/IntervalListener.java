package com.ilyaeremin.graphicmodule;

import android.support.annotation.FloatRange;

public interface IntervalListener {
    void onChanged(@FloatRange(from = 0, to = 100) float leftBound, @FloatRange(from = 0, to = 100) float rightBound);
}
