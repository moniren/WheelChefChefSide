package com.wheelchef.wheelchefchef.datamodels;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class OrderModel implements Serializable{

    public static final String ACCEPTANCE_ACCEPTED = "accepted";
    public static final String ACCEPTANCE_REJECTED = "rejected";

    private String orderId;
    private ArrayList<String> dishId;
    private ArrayList<Integer> amount;
    private String orderTime;
    private String customerName;
    private String recipientName;
    private String deliveryAddress;
    private String foodPrice;
    private String acceptanceStatus;

    public OrderModel(ArrayList<Integer> amount, String customerName, String deliveryAddress,
                      ArrayList<String> dishId, String foodPrice, String orderId, String orderTime, String recipientName,
                      String acceptanceStatus) {
        this.amount = amount;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.dishId = dishId;
        this.foodPrice = foodPrice;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.recipientName = recipientName;
        this.acceptanceStatus = acceptanceStatus;
    }

    public ArrayList<Integer> getAmount() {
        return amount;
    }

    public String getCutomerName() {
        return customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public ArrayList<String> getDishId() {
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

    public String getAcceptanceStatus() {
        return acceptanceStatus;
    }

    public void setAmount(ArrayList<Integer> amount) {
        this.amount = amount;
    }

    public void setCutomerName(String cutomerName) {
        this.customerName = cutomerName;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDishId(ArrayList<String> dishId) {
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

    public void setAcceptanceStatus(String acceptanceStatus) {
        this.acceptanceStatus = acceptanceStatus;
    }
}
