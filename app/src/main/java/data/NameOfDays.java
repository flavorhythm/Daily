package data;

/**
 * Created by zyuki on 2/8/2016.
 */
public enum NameOfDays {
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday"),
    SUN("Sunday");

    String dayOfWeek;
    NameOfDays(String dayOfWeek) {this.dayOfWeek = dayOfWeek;}

    @Override
    public String toString() {
        return this.dayOfWeek;
    }
}
