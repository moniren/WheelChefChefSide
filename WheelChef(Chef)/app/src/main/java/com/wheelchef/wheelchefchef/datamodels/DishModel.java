package com.wheelchef.wheelchefchef.datamodels;

import android.database.Cursor;

import com.wheelchef.wheelchefchef.sqlitedb.DishesTable;

import java.util.ArrayList;

/**
 * Created by lyk on 12/7/2015.
 */
public class DishModel {
    private String dishId;
    private String dishName;
    private String chefName;
    private String description;
    private float price;
    private int timesOrdered;
    private float discount;
    private String category;
    private String filePath;


    public DishModel(String dishId, String dishName, String chefName, String description, float price, int timesOrdered, float discount,
                     String category, String filePath){
        this.dishId = dishId;
        this.dishName = dishName;
        this.chefName = chefName;
        this.description = description;
        this.price = price;
        this.timesOrdered = timesOrdered;
        this.discount = discount;
        this.category = category;
        this.filePath = filePath;
    }

    public boolean useSamePhoto(DishModel dishModel){
        return this.filePath.equals(dishModel.dishName);
    }


    public boolean isSameDish(DishModel dishModel){
        return (this.dishId.equals(dishModel.dishId));
    }


    public boolean equalsTo(DishModel dishModel){
        if(!this.dishId.equals(dishModel.dishId))
            return false;
        else if(!this.dishName.equals(dishModel.dishName))
            return false;
        else if(!this.dishName.equals(dishModel.dishName))
            return false;
        else if(!this.chefName.equals(dishModel.chefName))
            return false;
        else if(this.price==dishModel.price)
            return false;
        else if(this.timesOrdered==dishModel.timesOrdered)
            return false;
        else if(this.discount==dishModel.discount)
            return false;
        else if(!this.category.equals(dishModel.dishName))
            return false;
        else if(!this.filePath.equals(dishModel.dishName))
            return false;
        else
            return true;

    }
    //the order of the fields will contribute to the return value, starting from 0
    public ArrayList<Integer> differencesFrom(DishModel dishModel){
        ArrayList<Integer> forReturn = new ArrayList<>();
        if(!this.dishId.equals(dishModel.dishId)){
            forReturn.add(0);
        }
        if(!this.dishName.equals(dishModel.dishName)){
            forReturn.add(1);
        }
        if(!this.dishName.equals(dishModel.dishName)){
            forReturn.add(2);
        }
        if(!this.chefName.equals(dishModel.chefName)){
            forReturn.add(3);
        }
        if(this.price==dishModel.price){
            forReturn.add(4);
        }
        if(this.timesOrdered==dishModel.timesOrdered){
            forReturn.add(5);
        }
        if(this.discount==dishModel.discount){
            forReturn.add(6);
        }
        if(!this.category.equals(dishModel.dishName)){
            forReturn.add(7);
        }
        if(!this.filePath.equals(dishModel.dishName)){
            forReturn.add(8);
        }
        return forReturn;
    }

    public static ArrayList<DishModel> getDishListFromCursor(Cursor cursor){
        ArrayList<DishModel> dishList = new ArrayList<>();

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String dishId = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISH_ID));
                String dishName = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISH_NAME));
                String chefName = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_CHEF_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DESCRIPTION));
                float price = cursor.getFloat(cursor.getColumnIndex(DishesTable.COLUMN_PRICE));
                int timesOrdered = cursor.getInt(cursor.getColumnIndex(DishesTable.COLUMN_TIMES_ORDERED));
                float discount = cursor.getFloat(cursor.getColumnIndex(DishesTable.COLUMN_DISCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_CATEGORY));
                String filePath = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_FILE_PATH));

                DishModel dishModel = new DishModel(dishId,dishName,chefName,description,price,
                        timesOrdered,discount,category,filePath);

                dishList.add(dishModel);

                cursor.moveToNext();
            }



        }

        return dishList;
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

    public String getFilePath() {
        return filePath;
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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
