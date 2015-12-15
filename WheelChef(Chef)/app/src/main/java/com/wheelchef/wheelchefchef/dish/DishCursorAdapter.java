package com.wheelchef.wheelchefchef.dish;

/**
 * Created by lyk on 12/12/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.main.MenuFragment;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.sqlitedb.DishesTable;
import com.wheelchef.wheelchefchef.utils.BitmapUtil;

/**
 * Created by John on 2015/8/19.
 */
public class DishCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    private MenuFragment menuFragment;
    private MainActivity mainActivity;

    private DeleteOnClickListener deleteOnClickListener;
    private EditOnClickListener editOnClickListener;

    private static class ViewHolder  {
        ImageView ivDishPhoto;
        TextView tvDishName;
        ImageButton deleteBtn;
        ImageButton editBtn;
    }


    private class DeleteOnClickListener implements View.OnClickListener {
        private int id;
        DeleteOnClickListener(int id){
            this.id = id;
        }

        @Override
        public void onClick(View v) {
//            notesFragment.showDeleteConfirmationDialog(id);
        }
    }

    private class EditOnClickListener implements View.OnClickListener {
        private String dishId;
        EditOnClickListener(String dishId){
            this.dishId = dishId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mainActivity,ViewDishDetailActivity.class);
            intent.putExtra(ViewDishDetailActivity.DISH_ID,dishId);
            mainActivity.startActivity(intent);
        }
    }

    public DishCursorAdapter(Context context, Cursor c, int flags, MenuFragment menuFragment) {
        super(context,c, flags);
        this.mainActivity = (MainActivity) context;
        this.menuFragment = menuFragment;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder  =   (ViewHolder)    view.getTag();
        String dishName = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISH_NAME));

        String dishId = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_DISH_ID));


        String photoString = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_PHOTO));

        byte[] photoAsBytes = Base64.decode(photoString, Base64.DEFAULT);

        Bitmap photo = BitmapFactory.decodeByteArray(photoAsBytes, 0, photoAsBytes.length);

        photo = BitmapUtil.getResizedBitmap(photo,photo.getWidth()/2,photo.getHeight()/2);
        holder.ivDishPhoto.setImageBitmap(photo);


        //int id = cursor.getInt(cursor.getColumnIndex(MainTable.COLUMN_ID));



        //deleteOnClickListener = new DeleteOnClickListener(id);
        editOnClickListener = new EditOnClickListener(dishId);


        holder.deleteBtn.setOnClickListener(deleteOnClickListener);
        holder.editBtn.setOnClickListener(editOnClickListener);

//        if(dishName.length() > 10){
//            dishName = dishName.substring(0,7)+"...";
//        }
        holder.tvDishName.setText(dishName);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View   view    =   inflater.inflate(R.layout.card_view_dish, null);
        ViewHolder holder  =   new ViewHolder();
        holder.ivDishPhoto = (ImageView) view.findViewById(R.id.imageview_dish_photo);
        holder.tvDishName    =   (TextView)  view.findViewById(R.id.textview_dish_name);
        holder.deleteBtn = (ImageButton) view.findViewById(R.id.btn_delete_dish);
        holder.editBtn = (ImageButton) view.findViewById(R.id.btn_edit_dish);
        view.setTag(holder);
        return view;
    }
}