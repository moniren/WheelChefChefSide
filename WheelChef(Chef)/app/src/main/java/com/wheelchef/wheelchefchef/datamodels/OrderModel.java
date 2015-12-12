package com.wheelchef.wheelchefchef.datamodels;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class OrderModel {
    private String orderId;
    private String dishId;
    private int amount;
    private String orderTime;
    private String customerName;
    private String recipientName;
    private String deliveryAddress;
    private String foodPrice;

    public OrderModel(int amount, String customerName, String deliveryAddress, String dishId, String foodPrice, String orderId, String orderTime, String recipientName) {
        this.amount = amount;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.dishId = dishId;
        this.foodPrice = foodPrice;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.recipientName = recipientName;
    }

    public int getAmount() {
        return amount;
    }

    public String getCutomerName() {
        return customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDishId() {
        return dishId;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCutomerName(String cutomerName) {
        this.customerName = cutomerName;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
