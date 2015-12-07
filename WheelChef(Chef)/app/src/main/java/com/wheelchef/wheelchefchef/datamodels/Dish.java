package com.wheelchef.wheelchefchef.datamodels;

/**
 * Created by lyk on 12/7/2015.
 */
public class Dish {
    private String dishId;
    private String dishName;
    private String chefName;
    private String description;
    private float price;
    private int timesOrdered;
    private float discount;
    private String category;


    public Dish(String dishId, String dishName, String chefName, String description, float price, int timesOrdered, float discount,
                String category){
        this.dishId = dishId;
        this.dishName = dishName;
        this.chefName = chefName;
        this.description = description;
        this.price = price;
        this.timesOrdered = timesOrdered;
        this.discount = discount;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getChefName() {
        return chefName;
    }

    public String getDescription() {
        return description;
    }

    public float getDiscount() {
        return discount;
    }

    public String getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public float getPrice() {
        return price;
    }

    public int getTimesOrdered() {
        return timesOrdered;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setTimesOrdered(int timesOrdered) {
        this.timesOrdered = timesOrdered;
    }
}
