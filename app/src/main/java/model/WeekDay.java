package model;

import java.util.List;

/**
 * Created by ZYuki on 2/9/2016.
 */
public class WeekDay extends Day {
    private List<String> lunchtime;

    public WeekDay() {}

    public List<String> getLunchtime() {return this.lunchtime;}

    public void setLunchtime(List<String> lunchtime) {this.lunchtime = lunchtime;}
}
