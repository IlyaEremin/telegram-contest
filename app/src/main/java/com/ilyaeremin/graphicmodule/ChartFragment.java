package com.ilyaeremin.graphicmodule;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class ChartFragment extends Fragment {

    private static final int DEFAULT_LEFT_BOUND  = 80;
    private static final int DEFAULT_RIGHT_BOUND = 100;

    private final static String KEY_CHART_JSON = "chart_json";

    public static ChartFragment newInstance(@NonNull String json) {
        Bundle args = new Bundle();
        args.putString(KEY_CHART_JSON, json);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String json;

    private ChartView    uiChart;
    private ChartView    uiChartPreview;
    private LinearLayout uiColumns;
    private SeekBar      uiLeftBound;
    private SeekBar      uiRightBound;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        json = getArguments().getString(KEY_CHART_JSON);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiChart = view.findViewById(R.id.chart);
        uiChartPreview = view.findViewById(R.id.chart_preview);
        uiColumns = view.findViewById(R.id.columns);
        uiLeftBound = view.findViewById(R.id.left_bound);
        uiRightBound = view.findViewById(R.id.right_bound);
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
        uiLeftBound.setOnSeekBarChangeListener(new OnSeekBarProgressListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                uiChart.setLeftBound(progress);
            }
        });

        uiRightBound.setOnSeekBarChangeListener(new OnSeekBarProgressListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                uiChart.setRightBound(progress);
            }
        });
    }

    private AppCompatCheckBox createCheckbox(@NonNull final Chart chart, final int position, @NonNull final ChartView uiChart, @NonNull final ChartView uiChartPreview) {
        final Column            column     = chart.columns.get(position);
        final AppCompatCheckBox uiCheckBox = new AppCompatCheckBox(getContext());
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
