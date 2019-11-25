package com.example.cta_t4.entity;

import java.util.Date;

public class Report {
    private Integer repId;
    private Double caloriesGoal;
    private Date date;
    private Double totalCaloriesBurned;
    private Double totalCaloriesConsumed;
    private Integer totalStepTaken;
    private User userId;

    public Report(Double caloriesGoal, Date date, Double totalCaloriesBurned, Double totalCaloriesConsumed, Integer totalStepTaken, User userId) {
        this.caloriesGoal = caloriesGoal;
        this.date = date;
        this.totalCaloriesBurned = totalCaloriesBurned;
        this.totalCaloriesConsumed = totalCaloriesConsumed;
        this.totalStepTaken = totalStepTaken;
        this.userId = userId;
    }

    public Integer getRepId() {
        return repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
    }

    public Double getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(Double caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }

    public void setTotalCaloriesBurned(Double totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    public Double getTotalCaloriesConsumed() {
        return totalCaloriesConsumed;
    }

    public void setTotalCaloriesConsumed(Double totalCaloriesConsumed) {
        this.totalCaloriesConsumed = totalCaloriesConsumed;
    }

    public Integer getTotalStepTaken() {
        return totalStepTaken;
    }

    public void setTotalStepTaken(Integer totalStepTaken) {
        this.totalStepTaken = totalStepTaken;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
