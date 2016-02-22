package dataAccess;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zyuki on 2/15/2016.
 */
public class DailyTable {
    private DailyTable() {}

    public static final String TABLE_NAME = "daily_table";

    public static final String KEY_ID = "_id";
    public static final String WEEK_KEY = "week_key";
    public static final String DAY_OF_WEEK = "day_of_week";
    public static final String DATE = "date";
    public static final String LUNCH_TODO = "lunch_todo";
    public static final String DAILY_TODO = "daily_todo";

    public static final String[] ALL_COLUMNS = new String[] {
            KEY_ID,
            WEEK_KEY,
            DAY_OF_WEEK,
            DATE,
            LUNCH_TODO,
            DAILY_TODO
    };

    private static final String CREATE_DAILY_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WEEK_KEY + " INTEGER NOT NULL, " +
            DATE + " INTEGER UNIQUE NOT NULL, " +
            DAY_OF_WEEK + " INTEGER NOT NULL, " +
            LUNCH_TODO + " TEXT, " +
            DAILY_TODO + " TEXT);";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILY_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
