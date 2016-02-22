package model;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import data.NameOfDays;

/**
 * Created by zyuki on 2/8/2016.
 */
public class Week implements Serializable {
    private List<Day> daysOfWeek;

    private int year;
    private int weekNum;
    private long startingDate;
    private String weekYearNum;

    public Week() {
        daysOfWeek = new ArrayList<>();
    }

    public Week(int year, int weekNum) {
        this.year = year;
        this.weekNum = weekNum;
        this.weekYearNum = generateYearNumString(year, weekNum);
        daysOfWeek = new ArrayList<>();

        this.startingDate = generateFirstOfWeek(year, weekNum);
    }

    private String generateYearNumString(int year, int weekNum) {
        final int ten = 10;
        String returnString = String.valueOf(year);

        if(weekNum < ten) {
            returnString += "0";
        }

        returnString += String.valueOf(weekNum);

        return returnString;
    }

    private long generateFirstOfWeek(int year, int weekNum) {
        final int firstMonth = 0;
        final int firstDay = 1;
        final int dateOffset = 2;

        Calendar cal = Calendar.getInstance();
        cal.set(year, firstMonth, firstDay);

        int startOfWeek = cal.get(Calendar.DAY_OF_WEEK) - dateOffset;
        cal.add(Calendar.DAY_OF_MONTH, -1 * startOfWeek);

        cal.add(Calendar.WEEK_OF_YEAR, weekNum);

        return cal.getTimeInMillis();
    }

    @SafeVarargs
    public final void addDay(NameOfDays name, List<String>... args) {
        switch(name) {
            case MON: case TUE: case WED: case THU: case FRI:
                List<String> afterWork = args[0];
                List<String> lunchtime = args[1];

                this.daysOfWeek.add(name.ordinal(), createWeekDay(name, afterWork, lunchtime));
                break;
            case SAT: case SUN:
                List<String> dailyToDo = args[0];

                this.daysOfWeek.add(name.ordinal(), createWeekEnd(name, dailyToDo));
                break;
        }
    }

    private final Day createWeekDay(NameOfDays name, List<String> dailyToDo, List<String> lunchtime) {
        WeekDay dayObj = new WeekDay();

        dayObj.setName(name);
        dayObj.setDailyToDo(dailyToDo);
        dayObj.setLunchtime(lunchtime);

        return dayObj;
    }

    private final Day createWeekEnd(NameOfDays name, List<String> dailyToDo) {
        WeekEnd dayObj = new WeekEnd();

        dayObj.setName(name);
        dayObj.setDailyToDo(dailyToDo);

        return dayObj;
    }

    public final Day getDay(NameOfDays name) {
        return this.daysOfWeek.get(name.ordinal());
    }

    @Nullable
    public final List<Day> getAllDays() {
        return this.daysOfWeek;
    }

    public void addDay(Day day) {
        this.daysOfWeek.add(day);
    }

    public int getYear() {return year;}

    public void setYear(int year) {this.year = year;}

    public int getWeekNum() {return weekNum;}

    public void setWeekNum(int weekNum) {this.weekNum = weekNum;}

    public long getStartingDate() {return this.startingDate;}

    public void setStartingDate(long startingDate) {
        this.startingDate = startingDate;
    }

    public String getWeekYearNum() {return this.weekYearNum;}
}
