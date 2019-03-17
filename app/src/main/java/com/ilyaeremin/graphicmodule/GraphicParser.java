package com.ilyaeremin.graphicmodule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class GraphicParser {

    static Graphic parse(@NonNull String jsonString) {
        try {
            JSONObject          json = new JSONObject(jsonString);
            Map<String, Column> hash = new HashMap<>();

            JSONObject colors = json.getJSONObject("colors");
            for (Iterator<String> it = colors.keys(); it.hasNext(); ) {
                String key    = it.next();
                Column column = getOrCreateAddAndGet(hash, key);
                column.color = Color.parseColor((String) colors.get(key));
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
                    Point     point      = new Point(columnX.getLong(i), columnJson.getInt(i));
                    column.points.add(point);
                }
            }

            Graphic graphic = new Graphic();
            graphic.columns = new ArrayList<>(hash.values());
            return graphic;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable private static Column getOrCreateAddAndGet(Map<String, Column> map, String key) {
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
