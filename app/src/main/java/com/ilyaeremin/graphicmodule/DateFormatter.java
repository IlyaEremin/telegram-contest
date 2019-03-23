package com.ilyaeremin.graphicmodule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    // TODO handle case when user change system language
    public final static SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
    private final static Date date = new Date();

    public static String format(long unix) {
        date.setTime(unix);
        return sdf.format(date);
    }
}
