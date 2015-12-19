package com.wheelchef.wheelchefchef.dish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;
import com.wheelchef.wheelchefchef.order.ViewOrderDetailActivity;

import java.util.ArrayList;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class DishListAdapter extends ArrayAdapter<String>{

    Context context;

    ArrayList<String> data;

    private OrderModel orderModel;

    private int resource;

    public DishListAdapter(Context context, int resource, ArrayList<String> objects,OrderModel orderModel) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orderModel = orderModel;
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DishHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DishHolder();
            holder.dishPic = (CircularImageView)row.findViewById(R.id.dish_list_dish_photo);
            holder.txtDishName = (TextView)row.findViewById(R.id.dish_list_dishName);
            holder.txtUnitPrice = (TextView)row.findViewById(R.id.dish_list_dishUnitPrice);
            holder.txtQty = (TextView)row.findViewById(R.id.dish_list_dishQty);
            holder.txtTotalPrice = (TextView)row.findViewById(R.id.dish_list_dishTotalPrice);
            //holder.wholeRow = (RelativeLayout) row.findViewById(R.id.row_order_list);

            row.setTag(holder);
        }
        else
        {
            holder = (DishHolder)row.getTag();
        }

        final double SAMPLE_UNIT_PRICE = 3.30;
        holder.txtDishName.setText(data.get(position));
        holder.txtQty.setText(" X "+orderModel.getAmount().get(position).toString());
        holder.txtTotalPrice.setText("S$ "+String.valueOf(Math.round(orderModel.getAmount().get(position) * SAMPLE_UNIT_PRICE* 100.0) / 100.0));
        holder.txtUnitPrice.setText("S$ "+SAMPLE_UNIT_PRICE);

        if(data.get(position).equals("Gong Bao Ji Ding"))
            holder.dishPic.setImageResource(R.drawable.dish_gbjd);
        else if (data.get(position).equals("Shui Zhu Niu Rou"))
            holder.dishPic.setImageResource(R.drawable.dish_sznr);


        /*if(order.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_ACCEPTED)){
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_green));
        } else if (order.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_REJECTED)){
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
        } else {
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_accent));
        }*/
        return row;
    }

    private static class DishHolder
    {
        CircularImageView dishPic;
        TextView txtDishName;
        TextView txtUnitPrice;
        TextView txtQty;
        TextView txtTotalPrice;
        //RelativeLayout wholeRow;
    }
}
