package com.zenoyuki.flavorhythm.daily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import data.DailyAdapter;
import data.NameOfDays;
import dataAccess.DataAccessObject;
import dataAccess.WeeklyTable;
import model.Day;
import model.Week;
import model.WeekDay;
import model.WeekEnd;

public class DisplayWeekActivity extends AppCompatActivity {
    private List<Day> daysInWeek;
    private ListView dailyList;
    private DailyAdapter dailyAdapter;

    private DataAccessObject dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_week);

        dataAccess = ((AppOverlay)getApplication()).dataAccess;

        dailyList = (ListView)findViewById(R.id.display_list_daily);

        daysInWeek = new ArrayList<>();
        dailyAdapter = new DailyAdapter(DisplayWeekActivity.this, R.layout.daily_row, daysInWeek);
        dailyList.setAdapter(dailyAdapter);

        Bundle extras = getIntent().getExtras();

        int year = extras.getInt(WeeklyTable.YEAR);
        int weekNum = extras.getInt(WeeklyTable.WEEK_NUM);

        findWeek(year, weekNum);
    }

    private void findWeek(int getYear, int getWeekNum) {
        Week week = dataAccess.getWeek(getYear, getWeekNum);
        List<Day> daysFromDatabase;

        if(week != null) {
            daysFromDatabase = week.getAllDays();

            if(daysFromDatabase != null) {
                for(Day dayInWeek : daysFromDatabase) {
                    NameOfDays name = dayInWeek.getName();

                    switch(name) {
                        case MON: case TUE: case WED: case THU: case FRI:
                            WeekDay weekDay = new WeekDay();
                            WeekDay castWeekDay = (WeekDay)dayInWeek;

                            weekDay.setName(name);
                            weekDay.setDateLong(castWeekDay.getDateLong());
                            weekDay.setLunchtime(castWeekDay.getLunchtime()); //TODO: Confirm this works
                            weekDay.setDailyToDo(castWeekDay.getDailyToDo());

                            daysInWeek.add(weekDay);
                            dailyAdapter.notifyDataSetChanged();
                            break;
                        case SAT: case SUN:
                            WeekEnd weekEnd = new WeekEnd();
                            WeekEnd castWeekEnd = (WeekEnd)dayInWeek;

                            weekEnd.setName(name);
                            weekEnd.setDateLong(castWeekEnd.getDateLong());
                            weekEnd.setDailyToDo(castWeekEnd.getDailyToDo());

                            daysInWeek.add(weekEnd);
                            dailyAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            } else {

            }
        }
    }
}
