package com.zenoyuki.flavorhythm.daily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import data.OverviewAdapter;

public class OverviewActivity extends AppCompatActivity {
    private ListView list;
    private OverviewAdapter overviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.overview_list_week);
        overviewAdapter = new OverviewAdapter(OverviewActivity.this, R.layout.overview_row, getCurrentYear());

        list.setAdapter(overviewAdapter);
    }

    private int getCurrentYear() {
        return 2016;
    }
}
