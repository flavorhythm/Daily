package dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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

        try {
            generateWeeks();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        this.context = context;
    }

    private void generateWeeks() throws SQLException {
        int[] years = new int[2];
        years[0] = Calendar.getInstance().get(Calendar.YEAR);
        years[1] = years[0] + 1;

        open();

        Cursor weeklyCursor = db.query(
                WeeklyTable.TABLE_NAME,
                WeeklyTable.ALL_COLUMNS,
                WeeklyTable.YEAR + " =? OR " + WeeklyTable.YEAR + " =?",
                new String[]{String.valueOf(years[0]), String.valueOf(years[1])},
                null,
                null,
                WeeklyTable.WEEK_DATE + " DESC"
        );

        if(!weeklyCursor.moveToFirst()) {
            for(int year : years) {
                insertOneYear(year);
            }
        }else if(weeklyCursor.getInt(weeklyCursor.getColumnIndex(WeeklyTable.YEAR)) == years[1]) {
            insertOneYear(years[1]);
        }

        weeklyCursor.close();
        close();
    }

    private void insertOneYear(int year) {
        final int weeksInYear = 52;
        final int daysInWeek = 7;
        final int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        final int firstMonth = 0;
        final int firstDay = 1;
        final int dateOffset = 2;

        Calendar cal = Calendar.getInstance();
        cal.set(year, firstMonth, firstDay);

        int startOfWeek = cal.get(Calendar.DAY_OF_WEEK) - dateOffset;
        cal.add(Calendar.DAY_OF_MONTH, -1 * startOfWeek);

        for(int weekNum = 1; weekNum <= weeksInYear; weekNum++) {
            ContentValues weeklyValues = new ContentValues();

            String weekYearNum = String.valueOf(year);
            if(weekNum < 10) {weekYearNum += "0";}
            weekYearNum += weekNum;

            weeklyValues.put(WeeklyTable.WEEK_YEAR_NUM, weekYearNum);
            weeklyValues.put(WeeklyTable.YEAR, year);
            weeklyValues.put(WeeklyTable.WEEK_NUM, weekNum);

            long entryId = db.insert(WeeklyTable.TABLE_NAME, null, weeklyValues);

            if(year == thisYear) {
                for(int dayOfWeek = 0; dayOfWeek < daysInWeek; dayOfWeek++) {
                    generateDays(dayOfWeek, cal.getTimeInMillis(), entryId);

                    Log.v("input", String.valueOf(dayOfWeek));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
    }

    private void generateDays(int day, long date, long foreignKey) {
        ContentValues dailyValues = new ContentValues();
        dailyValues.put(DailyTable.WEEK_KEY, foreignKey);
        dailyValues.put(DailyTable.DATE, date);
        dailyValues.put(DailyTable.DAY_OF_WEEK, day);

        db.insert(DailyTable.TABLE_NAME, null, dailyValues);
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
        weeklyValues.put(WeeklyTable.YEAR, week.getYear());
        weeklyValues.put(WeeklyTable.WEEK_NUM, week.getWeekNum());
        weeklyValues.put(WeeklyTable.WEEK_DATE, week.getStartingDate());

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

    @Nullable
    public Week getWeek(int getYear, int getWeek) {
        List<Week> weekList = new ArrayList<>();

        Log.v("dataAccessData", String.valueOf(getYear));
        Log.v("dataAccessData", String.valueOf(getWeek));

        Cursor weeklyCursor = db.query(
                WeeklyTable.TABLE_NAME,
                WeeklyTable.ALL_COLUMNS,
                WeeklyTable.YEAR + " =? AND " + WeeklyTable.WEEK_NUM + " =?",
                new String[] {String.valueOf(getYear), String.valueOf(getWeek)},
                null,
                null,
                WeeklyTable.WEEK_DATE + " ASC"
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
                        DailyTable.DATE + " ASC"
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

        try{
            int dividerIndex = toDos.indexOf(Day.DIVIDER);

            while(dividerIndex > 0) {
                toDoList.add(toDos.substring(0, dividerIndex));

                toDos = toDos.substring(dividerIndex + Day.DIVIDER.length());
                dividerIndex = toDos.indexOf(Day.DIVIDER);
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        } finally {
            toDoList.add("Nothing to do");
        }

        return toDoList;
    }
}
