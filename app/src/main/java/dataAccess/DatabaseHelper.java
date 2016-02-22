package dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zyuki on 2/15/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper thisInstance;

    private static final String DATABASE_NAME = "daily_db";
    private static final int DATABASE_VERSION = 6;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if(thisInstance == null) {
            thisInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return thisInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WeeklyTable.onCreate(db);
        DailyTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        WeeklyTable.onUpgrade(db);
        DailyTable.onUpgrade(db);
    }
}
