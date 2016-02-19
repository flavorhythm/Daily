package dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.NameOfDays;
import model.Day;
import model.Week;
import model.WeekDay;
import model.WeekEnd;

/**
 * Created by zyuki on 2/15/2016.
 */
public class DataAccessObject {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private Context context;

    public DataAccessObject(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);

        this.context = context;
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public long updateWeek(Week week) {
        ContentValues weeklyValues = new ContentValues();
        weeklyValues.put(WeeklyTable.WEEK_DATE, week.getStartingDate());
        weeklyValues.put(WeeklyTable.WEEK_YEAR_NUM, week.getWeekYearNum());

        long entryId = db.replace(WeeklyTable.TABLE_NAME, null, weeklyValues);

        saveAllDays(week.getAllDays(), entryId);

        return entryId;
    }

    private void saveAllDays(List<Day> daysList, long foreignKey) {
        ContentValues values = new ContentValues();

        for(Day dayOfWeek : daysList) {
            switch(dayOfWeek.getName()) {
                case MON: case TUE: case WED: case THU: case FRI:
                    values = saveDay((WeekDay)dayOfWeek, foreignKey);
                    break;
                case SAT: case SUN:
                    values = saveDay((WeekEnd)dayOfWeek, foreignKey);
                    break;
            }

            long entryId = db.replace(DailyTable.TABLE_NAME, null, values);
        }
    }

    private ContentValues saveDay(WeekDay day, long foreignKey) {
        ContentValues dailyValues = new ContentValues();
        dailyValues.put(DailyTable.DATE, day.getDateLong());
        dailyValues.put(DailyTable.DAY_OF_WEEK, day.getName().ordinal());
        dailyValues.put(DailyTable.WEEK_KEY, foreignKey);
        dailyValues.put(DailyTable.LUNCH_TODO, day.getLunchtimeString());
        dailyValues.put(DailyTable.DAILY_TODO, day.getToDoString());

        return dailyValues;
    }

    private ContentValues saveDay(WeekEnd day, long foreignKey) {
        ContentValues dailyValues = new ContentValues();
        dailyValues.put(DailyTable.DATE, day.getDateLong());
        dailyValues.put(DailyTable.DAY_OF_WEEK, day.getName().ordinal());
        dailyValues.put(DailyTable.WEEK_KEY, foreignKey);
        dailyValues.put(DailyTable.DAILY_TODO, day.getToDoString());

        return dailyValues;
    }

    public Week getWeek(int getYear, int getWeek) {
        List<Week> weekList = new ArrayList<>();

        Cursor weeklyCursor = db.query(
                WeeklyTable.TABLE_NAME,
                WeeklyTable.ALL_COLUMNS,
                WeeklyTable.YEAR + " =? AND " + WeeklyTable.WEEK_NUM + " =?",
                new String[] {String.valueOf(getYear), String.valueOf(getWeek)},
                null,
                null,
                WeeklyTable.WEEK_DATE + " DESC"
        );

        if(weeklyCursor.moveToFirst()) {
            do {
                Week week = new Week();

                int foreignKey = weeklyCursor.getInt(weeklyCursor.getColumnIndex(WeeklyTable.KEY_ID));

                Cursor dailyCursor = db.query(
                        DailyTable.TABLE_NAME,
                        DailyTable.ALL_COLUMNS,
                        DailyTable.WEEK_KEY + " =?",
                        new String[] {String.valueOf(foreignKey)},
                        null,
                        null,
                        DailyTable.DATE + " DESC"
                );

                week.setWeekNum(weeklyCursor.getInt(weeklyCursor.getColumnIndex(WeeklyTable.WEEK_NUM)));
                week.setYear(weeklyCursor.getInt(weeklyCursor.getColumnIndex(WeeklyTable.YEAR)));
                week.setStartingDate(weeklyCursor.getLong(weeklyCursor.getColumnIndex(WeeklyTable.WEEK_DATE)));

                if(dailyCursor.moveToFirst()) {
                    do {
                        int dayOfWeek = dailyCursor.getInt(dailyCursor.getColumnIndex(DailyTable.DAY_OF_WEEK));
                        NameOfDays name = NameOfDays.values()[dayOfWeek];

                        switch(name) {
                            case MON: case TUE: case WED: case THU: case FRI:
                                WeekDay weekDay = new WeekDay();

                                weekDay.setName(name);
                                weekDay.setDateLong(dailyCursor.getLong(dailyCursor.getColumnIndex(DailyTable.DATE)));
                                weekDay.setLunchtime(parseToDoString(dailyCursor.getString(dailyCursor.getColumnIndex(DailyTable.LUNCH_TODO))));
                                weekDay.setDailyToDo(parseToDoString(dailyCursor.getString(dailyCursor.getColumnIndex(DailyTable.DAILY_TODO))));

                                week.addDay(weekDay);
                                break;
                            case SAT: case SUN:
                                WeekEnd weekEnd = new WeekEnd();

                                weekEnd.setName(name);
                                weekEnd.setDateLong(dailyCursor.getLong(dailyCursor.getColumnIndex(DailyTable.DATE)));
                                weekEnd.setDailyToDo(parseToDoString(dailyCursor.getString(dailyCursor.getColumnIndex(DailyTable.DAILY_TODO))));

                                week.addDay(weekEnd);
                                break;
                        }

                    } while(dailyCursor.moveToNext());
                }

                dailyCursor.close();

                weekList.add(week);
            } while(weeklyCursor.moveToNext());
        }

        weeklyCursor.close();

        return weekList.get(0);
    }

    private List<String> parseToDoString(String toDos) {
        List<String> toDoList = new ArrayList<>();

        int dividerIndex = toDos.indexOf(Day.DIVIDER);

        while(dividerIndex > 0) {
            toDoList.add(toDos.substring(0, dividerIndex));

            toDos = toDos.substring(dividerIndex + Day.DIVIDER.length());
            dividerIndex = toDos.indexOf(Day.DIVIDER);
        }

        return toDoList;
    }
}
