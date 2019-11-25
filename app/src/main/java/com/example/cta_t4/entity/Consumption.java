package com.example.cta_t4.entity;

import java.util.Date;

public class Consumption {

    private Integer conId;
    private Date date;
    private Double quantityServing;
    private Food foodId;
    private User userId;

    public Consumption() {
    }

    public Consumption(Integer conId, Date date, Double quantityServing, Food foodId, User userId) {
        this.conId = conId;
        this.date = date;
        this.quantityServing = quantityServing;
        this.foodId = foodId;
        this.userId = userId;
    }

    public Consumption(Date date, Double quantityServing, Food foodId, User userId) {
        this.date = date;
        this.quantityServing = quantityServing;
        this.foodId = foodId;
        this.userId = userId;
    }

    public Integer getConId() {
        return conId;
    }

    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getQuantityServing() {
        return quantityServing;
    }

    public void setQuantityServing(Double quantityServing) {
        this.quantityServing = quantityServing;
    }

    public Food getFoodId() {
        return foodId;
    }

    public void setFoodId(Food foodId) {
        this.foodId = foodId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
