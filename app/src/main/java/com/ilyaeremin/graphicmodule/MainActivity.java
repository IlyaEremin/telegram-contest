package com.ilyaeremin.graphicmodule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> chartsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.title_statistics);
        ViewPager uiViewPager = findViewById(R.id.pager);
        String    json        = RawReader.readFile(this, R.raw.chart_data);
        if (json != null) {
            chartsJson = ChartParser.parse(json);
        }
        uiViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return chartsJson.size();
            }

            @Override
            public Fragment getItem(int i) {
                return ChartFragment.newInstance(chartsJson.get(i));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "#" + String.valueOf(position);
            }
        });
    }
}
