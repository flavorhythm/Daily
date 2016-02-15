package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.NameOfDays;

/**
 * Created by zyuki on 2/8/2016.
 */
public class Week {
    private List<Day> daysOfWeek;

    private int year;
    private int weekNum;
    private long startingDate;

    public Week() {
        daysOfWeek = new ArrayList<>();
    }

    public final void addDay(NameOfDays name, List<String>... args) {
        switch(name) {
            case MON: case TUE: case WED: case THU: case FRI:
                this.daysOfWeek.add(name.ordinal(), createWeekDay(name, args[0], args[1]));
                break;
            case SAT: case SUN:
                this.daysOfWeek.add(name.ordinal(), createWeekEnd(name, args[0]));
                break;
        }
    }

    private final Day createWeekDay(NameOfDays name, List<String> lunchtime, List<String> dailyToDo) {
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

    public final List<Day> getAllDays() {
        return this.daysOfWeek;
    }

    public int getYear() {return year;}

    public void setYear(int year) {this.year = year;}

    public int getWeekNum() {return weekNum;}

    public void setWeekNum(int weekNum) {this.weekNum = weekNum;}
}
