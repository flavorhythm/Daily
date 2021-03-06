package com.zenoyuki.flavorhythm.daily;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.DailyAdapter;
import data.NameOfDays;
import data.OverviewAdapter;
import dataAccess.DataAccessObject;
import dataAccess.WeeklyTable;
import model.Day;
import model.Week;
import model.WeekDay;
import model.WeekEnd;

public class MainActivity extends AppCompatActivity {
    private List<Day> dailyList;
    private ListView listView;

    private DataAccessObject dataAccess;
    private DailyAdapter dailyAdapter;

    private TextView displayWeekNum, displayYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataAccess = ((AppOverlay)getApplication()).dataAccess;

        displayWeekNum = (TextView)findViewById(R.id.main_text_weekNum);
        displayYear = (TextView)findViewById(R.id.main_text_year);

        dailyList = new ArrayList<>();
        dailyAdapter = new DailyAdapter(MainActivity.this, R.layout.daily_row, dailyList);

        listView = (ListView)findViewById(R.id.main_list_daily);
        listView.setAdapter(dailyAdapter);

        findThisWeek();
    }

    private void findThisWeek() {
        int thisYear;
        int thisWeek;

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            thisYear = Calendar.getInstance().get(Calendar.YEAR);
            thisWeek = findWeekNum(OverviewAdapter.findFirstDayOfYear(thisYear));
        } else {
            thisYear = bundle.getInt(WeeklyTable.YEAR);
            thisWeek = bundle.getInt(WeeklyTable.WEEK_NUM);
        }

        displayWeekNum.setText(String.valueOf(thisWeek));
        displayYear.setText(String.valueOf(thisYear));

        Week week = dataAccess.getWeek(thisYear, thisWeek);
        List<Day> daysFromDatabase;

        if(week != null) {
            daysFromDatabase = week.getAllDays();

            if(daysFromDatabase != null) {
                for(Day dayInWeek : daysFromDatabase) {
                    NameOfDays name = dayInWeek.getName();

                    switch (name) {
                        case MON: case TUE: case WED: case THU: case FRI:
                            WeekDay weekDay = new WeekDay();
                            WeekDay castWeekDay = (WeekDay)dayInWeek;

                            weekDay.setName(name);
                            weekDay.setDateLong(castWeekDay.getDateLong());
                            weekDay.setLunchtime(castWeekDay.getLunchtime()); //TODO: Confirm this works
                            weekDay.setDailyToDo(castWeekDay.getDailyToDo());

                            dailyList.add(weekDay);
                            dailyAdapter.notifyDataSetChanged();
                            break;
                        case SAT: case SUN:
                            WeekEnd weekEnd = new WeekEnd();
                            WeekEnd castWeekEnd = (WeekEnd)dayInWeek;

                            weekEnd.setName(name);
                            weekEnd.setDateLong(castWeekEnd.getDateLong());
                            weekEnd.setDailyToDo(castWeekEnd.getDailyToDo());

                            dailyList.add(weekEnd);
                            dailyAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        }
    }

    private int findWeekNum(Calendar firstOfYear) {
        final int daysInWeek = 7;
        Calendar current = Calendar.getInstance();

        current.setTimeInMillis(current.getTimeInMillis() - firstOfYear.getTimeInMillis());
        int deltaDays = current.get(Calendar.DAY_OF_YEAR);

        Double flooredValue = Math.floor(deltaDays / daysInWeek);

        return flooredValue.intValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, OverviewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
