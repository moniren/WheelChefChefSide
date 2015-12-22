package com.wheelchef.wheelchefchef.dish;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.base.CustomToolbarActivity;
import com.wheelchef.wheelchefchef.sqlitedb.DishesDataSource;
import com.wheelchef.wheelchefchef.sqlitedb.DishesTable;

/**
 * Created by lyk on 12/13/2015.
 */
public class ViewDishDetailActivity extends CustomToolbarActivity {
    public static final String DISH_ID = "dish_id";

    private TextView tvDishName, tvDishDesc, tvDishPrice, tvDishDiscount, tvDishCategory;
    private ImageView ivDishPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dish_detail);

        ivDishPhoto = (ImageView) findViewById(R.id.imageview_dish_photo);
        tvDishName = (TextView) findViewById(R.id.textview_dish_name);
        tvDishDesc = (TextView) findViewById(R.id.textview_dish_desc);
        tvDishPrice = (TextView) findViewById(R.id.textview_dish_price);
        tvDishDiscount = (TextView) findViewById(R.id.textview_dish_discount);
        tvDishCategory = (TextView) findViewById(R.id.textview_dish_category);


        setUpToolbar();
        setUpData();

    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        String title = getResources().getString(R.string.title_activity_view_dish_detail);
        toolbar.setTitle(title);
    }

    private void setUpData(){
        Intent intent = getIntent();
        String dishId = intent.getStringExtra(DISH_ID);
        if(dishId != null && dishId.length() > 0){
            Cursor cursor = DishesDataSource.getCursorFromDishId(dishId);
            if(cursor!=null && cursor.getCount() > 0){
                cursor.moveToFirst();
                tvDishName.setText(cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISH_NAME)));
                tvDishDesc.setText(cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DESCRIPTION)));
                tvDishPrice.setText(cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_PRICE)));
                tvDishDiscount.setText(cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISCOUNT)));
                tvDishCategory.setText(cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_CATEGORY)));

                String photoString = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_PHOTO));
                byte[] photoAsBytes = Base64.decode(photoString, Base64.DEFAULT);
                ivDishPhoto.setImageBitmap(BitmapFactory.decodeByteArray(photoAsBytes, 0, photoAsBytes.length));
            }
        }
    }




}
