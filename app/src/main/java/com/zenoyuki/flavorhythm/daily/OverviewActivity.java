package com.zenoyuki.flavorhythm.daily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import data.OverviewAdapter;

public class OverviewActivity extends AppCompatActivity {
    ListView list;

    //TODO: get all weeks and send one week data to "setonitemclicklistener"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.overview_list_week);
        OverviewAdapter adapter = new OverviewAdapter(OverviewActivity.this, R.layout.overview_row, getCurrentYear());
        list.setAdapter(adapter);
    }

    private int getCurrentYear() {
        return 2016;
    }
}
