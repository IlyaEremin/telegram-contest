package com.ilyaeremin.graphicmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> chartsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.title_statistics);
        ListView uiListView = findViewById(R.id.list);
        String   json       = RawReader.readFile(this, R.raw.chart_data);
        if (json != null) {
            chartsJson = ChartParser.parse(json);
        }
        List<String> names = new ArrayList<>();
        for (int i = 0; i < chartsJson.size(); i++) {
            names.add("#" + String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        uiListView.setAdapter(adapter);
        uiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChartActivity.start(MainActivity.this, chartsJson.get(position));
            }
        });
    }
}
