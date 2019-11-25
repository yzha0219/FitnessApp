package com.example.cta_t4.entity;

public class Food {

    private Integer foodId;
    private Double calorieAmount;
    private String category;
    private Double fat;
    private String name;
    private Double servingAmount;
    private String servingUnit;

    public Food() {
    }

    public Food(Integer foodId) {
        this.foodId = foodId;
    }

    public Food(Integer foodId, Double calorieAmount, String category, Double fat, String name, Double servingAmount, String servingUnit) {
        this.foodId = foodId;
        this.calorieAmount = calorieAmount;
        this.category = category;
        this.fat = fat;
        this.name = name;
        this.servingAmount = servingAmount;
        this.servingUnit = servingUnit;
    }

    public Food(Double calorieAmount, String category, Double fat, String name, Double servingAmount, String servingUnit) {
        this.calorieAmount = calorieAmount;
        this.category = category;
        this.fat = fat;
        this.name = name;
        this.servingAmount = servingAmount;
        this.servingUnit = servingUnit;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Double getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(Double calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(Double servingAmount) {
        this.servingAmount = servingAmount;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }
}
