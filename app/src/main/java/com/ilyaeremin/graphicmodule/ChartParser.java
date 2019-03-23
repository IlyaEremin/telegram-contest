package com.ilyaeremin.graphicmodule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ilyaeremin.graphicmodule.utils.ColorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ChartParser {

    static List<String> parse(@NonNull String json) {
        try {
            List<String> charts    = new ArrayList<>();
            JSONArray    jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                charts.add(jsonArray.getString(i));
            }
            return charts;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Chart parseChart(@NonNull String jsonString) {
        try {
            JSONObject          json = new JSONObject(jsonString);
            Map<String, Column> hash = new HashMap<>();

            JSONObject colors = json.getJSONObject("colors");
            for (Iterator<String> it = colors.keys(); it.hasNext(); ) {
                String key    = it.next();
                Column column = getOrCreateAddAndGet(hash, key);
                column.color = ColorUtils.parseColor((String) colors.get(key));
            }
            JSONObject names = json.getJSONObject("names");
            for (Iterator<String> it = names.keys(); it.hasNext(); ) {
                String key    = it.next();
                Column column = getOrCreateAddAndGet(hash, key);
                column.name = names.getString(key);
            }

            JSONObject types = json.getJSONObject("types");
            for (Iterator<String> it = types.keys(); it.hasNext(); ) {
                String key    = it.next();
                Column column = getOrCreateAddAndGet(hash, key);
                String type   = types.getString(key);
                switch (type) {
                    case "line":
                        column.type = Type.LINE;
                        break;
                }
            }

            JSONArray columns = json.getJSONArray("columns");
            JSONArray columnX = columns.getJSONArray(0);
            for (int i = 1; i < columnX.length(); i++) {
                for (int j = 1; j < columns.length(); j++) {
                    JSONArray columnJson = columns.getJSONArray(j);
                    String    columnKey  = columnJson.getString(0);
                    Column    column     = getOrCreateAddAndGet(hash, columnKey);
                    long      x          = columnX.getLong(i);
                    float     y          = (float) columnJson.getDouble(i);
                    if (column.ys == null) {
                        column.ys = new float[(columnX.length() - 1)];
                    }
                    if (column.xs == null) {
                        column.xs = new long[(columnX.length() - 1)];
                    }
                    column.xs[i - 1] = x;
                    column.ys[i - 1] = y;
                }
            }

            Chart chart = new Chart();
            chart.columns = new ArrayList<>(hash.values());
            return chart;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static Column getOrCreateAddAndGet(Map<String, Column> map, String key) {
        Column v;
        if (((v = map.get(key)) != null) || map.containsKey(key)) {
            return v;
        } else {
            if ("x".equals(key)) {
                return null;
            }
            Column defaultValue = new Column(key);
            map.put(key, defaultValue);
            return defaultValue;
        }
    }
}
