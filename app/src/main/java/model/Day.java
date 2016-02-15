package model;

import java.util.List;

import data.NameOfDays;

/**
 * Created by zyuki on 2/8/2016.
 */
public class Day {
    private List<String> dailyToDo;

    private long dateLong;
    private NameOfDays name;
    private boolean repeating;

    protected Day() {}

    public List<String> getDailyToDo() {return dailyToDo;}
    public long getDateLong() {return dateLong;}
    public NameOfDays getName() {return name;}
    public boolean isRepeating() {return repeating;}

    public void setDailyToDo(List<String> dailyToDo) {this.dailyToDo = dailyToDo;}
    public void setDateLong(long dateLong) {this.dateLong = dateLong;}
    public void setName(NameOfDays name) {this.name = name;}
    public void setRepeating(boolean repeating) {this.repeating = repeating;}
}
