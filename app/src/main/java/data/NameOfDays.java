package data;

/**
 * Created by zyuki on 2/8/2016.
 */
public enum NameOfDays {
    MON("monday"),
    TUE("tuesday"),
    WED("wednesday"),
    THU("thursday"),
    FRI("friday"),
    SAT("saturday"),
    SUN("sunday");

    String dayOfWeek;
    NameOfDays(String dayOfWeek) {this.dayOfWeek = dayOfWeek;}
}
//public final class NameOfDays {
//    private NameOfDays() {}
//
//    public static final String MON = "monday";
//    public static final String TUE = "tuesday";
//    public static final String WED = "wednesday";
//    public static final String THU = "thursday";
//    public static final String FRI = "friday";
//    public static final String SAT = "saturday";
//    public static final String SUN = "sunday";
//}
