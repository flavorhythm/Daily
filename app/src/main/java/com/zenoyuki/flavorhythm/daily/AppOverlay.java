package com.zenoyuki.flavorhythm.daily;

import android.app.Application;

import java.sql.SQLException;

import dataAccess.DataAccessObject;

/**
 * Created by zyuki on 2/15/2016.
 */
public class AppOverlay extends Application {
    public DataAccessObject dataAccess;

    @Override
    public void onCreate() {
        this.dataAccess = new DataAccessObject(getApplicationContext());

        openDatabaseAndTry();

        super.onCreate();
    }

    @Override
    public void onTerminate() {

        this.dataAccess.close();

        super.onTerminate();
    }

    private void openDatabaseAndTry() {
        try {
            this.dataAccess.open();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
