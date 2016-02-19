package dataAccess;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zyuki on 2/15/2016.
 */
public class WeeklyTable {
    private WeeklyTable() {}

    public static final String TABLE_NAME = "weekly_table";

    public static final String KEY_ID = "_id";
    public static final String YEAR = "year";
    public static final String WEEK_NUM = "week_number";
    public static final String WEEK_DATE = "date_of_week";

    public static final String WEEK_YEAR_NUM = "week_year_num";

    public static final String[] ALL_COLUMNS = new String[] {
            KEY_ID,
            WEEK_YEAR_NUM,
            WEEK_NUM,
            YEAR,
            WEEK_DATE
    };

    private static final String CREATE_WEEK_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WEEK_YEAR_NUM + " TEXT UNIQUE NOT NULL, " +
            WEEK_NUM + " INTEGER NOT NULL" +
            YEAR + " INTEGER NOT NULL" +
            WEEK_DATE + " INTEGER NOT NULL);";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEEK_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_WEEK_TABLE);
        onCreate(db);
    }
}
