package com.ilyaeremin.graphicmodule;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class ChartActivity extends AppCompatActivity {

    private static final int DEFAULT_LEFT_BOUND  = 80;
    private static final int DEFAULT_RIGHT_BOUND = 100;

    private final static String KEY_CHART_JSON = "chart_json";

    public static void start(@NonNull Context context, @NonNull String json) {
        Intent intent = new Intent(context, ChartActivity.class);
        intent.putExtra(KEY_CHART_JSON, json);
        context.startActivity(intent);
    }


    private String json;

    private ChartView    uiChart;
    private ChartView    uiChartPreview;
    private LinearLayout uiColumns;
    private IntervalView uiInterval;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        json = getIntent().getStringExtra(KEY_CHART_JSON);
        uiChart = findViewById(R.id.chart);
        uiChartPreview = findViewById(R.id.chart_preview);
        uiColumns = findViewById(R.id.columns);
        uiInterval = findViewById(R.id.interval);
    }

    @Override
    public void onResume() {
        super.onResume();

        final Chart chart = ChartParser.parseChart(json);

        uiChart.setChart(chart);
        uiChart.setInterval(DEFAULT_LEFT_BOUND, DEFAULT_RIGHT_BOUND);
        uiChartPreview.setChart(chart);

        uiColumns.removeAllViews();
        for (int i = 0; i < chart.columns.size(); i++) {
            uiColumns.addView(createCheckbox(chart, i, uiChart, uiChartPreview));
        }
        uiInterval.setOnIntervalChangeListener(new IntervalListener() {
            @Override
            public void onChanged(int leftBound, int rightBound) {
                uiChart.setInterval(leftBound, rightBound);
            }
        });
    }

    private AppCompatCheckBox createCheckbox(@NonNull final Chart chart, final int position, @NonNull final ChartView uiChart, @NonNull final ChartView uiChartPreview) {
        final Column            column     = chart.columns.get(position);
        final AppCompatCheckBox uiCheckBox = new AppCompatCheckBox(this);
        uiCheckBox.setText(column.name);
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(uiCheckBox, ColorStateList.valueOf(column.color));
        } else {
            uiCheckBox.setButtonTintList(ColorStateList.valueOf(column.color));
        }
        final float scale        = this.getResources().getDisplayMetrics().density;
        final int   extraPadding = (int) (10.0f * scale + 0.5f);

        uiCheckBox.setChecked(true);
        uiCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        uiCheckBox.setPadding(uiCheckBox.getPaddingLeft() + extraPadding,
            uiCheckBox.getPaddingTop() + extraPadding,
            uiCheckBox.getPaddingRight(),
            uiCheckBox.getPaddingBottom() + extraPadding);

        uiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiChart.setColumnVisibility(position, isChecked);
                uiChartPreview.setColumnVisibility(position, isChecked);
            }
        });
        return uiCheckBox;
    }
}
