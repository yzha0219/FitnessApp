package com.example.cta_t4.SQLite;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyStepsDao {
    @Query("SELECT * FROM dailysteps")
    List<DailySteps> getAll();
    @Query("SELECT * FROM dailySteps WHERE User_ID = :userId")
    List<DailySteps> findByUserId(int userId);
    @Query("SELECT * FROM dailysteps WHERE dailyStepId = :stepId LIMIT 1")
    DailySteps findByID(int stepId);
    @Insert
    long insert(DailySteps dailySteps);
    @Update(onConflict = REPLACE)
    public void updateUsers(DailySteps...dailySteps);
    @Delete
    void delete(DailySteps dailySteps);
    @Query("DELETE FROM dailysteps")
    void deleteAll();
}
