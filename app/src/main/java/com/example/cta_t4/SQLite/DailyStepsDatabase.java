package com.example.cta_t4.SQLite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DailySteps.class},version = 2,exportSchema = false)
public abstract class DailyStepsDatabase extends RoomDatabase {
    public abstract DailyStepsDao dailyStepsDao();
    private static volatile DailyStepsDatabase INSTANCE;
    static DailyStepsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DailyStepsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DailyStepsDatabase.class, "dailysteps_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
