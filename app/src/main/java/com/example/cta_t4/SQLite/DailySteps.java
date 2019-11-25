package com.example.cta_t4.SQLite;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class DailySteps {
    @PrimaryKey(autoGenerate = true)
    private Integer dailyStepId;
    @ColumnInfo(name = "User_ID")
    private Integer userId;
    @ColumnInfo(name = "Time")
    private String time;
    @ColumnInfo(name = "Steps")
    private Integer steps;
    @ColumnInfo(name = "Calorie_Goal")
    private Double calorie_goal;

    public DailySteps(Integer userId, String time, Integer steps, Double calorie_goal) {
        this.userId = userId;
        this.time = time;
        this.steps = steps;
        this.calorie_goal = calorie_goal;
    }

    public Integer getDailyStepId() {
        return dailyStepId;
    }

    public void setDailyStepId(Integer dailyStepId) {
        this.dailyStepId = dailyStepId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Double getCalorie_goal() {
        return calorie_goal;
    }

    public void setCalorie_goal(Double calorie_goal) {
        this.calorie_goal = calorie_goal;
    }
}
