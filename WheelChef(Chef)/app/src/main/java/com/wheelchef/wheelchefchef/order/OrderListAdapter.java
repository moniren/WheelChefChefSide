package com.wheelchef.wheelchefchef.order;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class OrderListAdapter extends ArrayAdapter<OrderModel>{

    Context context;

    ArrayList<OrderModel> data;

    private int resource;

    public OrderListAdapter(Context context, int resource, ArrayList<OrderModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OrderHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new OrderHolder();
            holder.userPic = (ImageView)row.findViewById(R.id.order_list_user_photo);
            holder.txtPrice = (TextView)row.findViewById(R.id.order_list_orderPrice);
            holder.txtTime = (TextView)row.findViewById(R.id.order_list_orderPrice);
            holder.btn = (ImageButton)row.findViewById(R.id.btn_view_order_detail);

            row.setTag(holder);
        }
        else
        {
            holder = (OrderHolder)row.getTag();
        }

        OrderModel order = data.get(position);
        holder.txtPrice.setText(order.getFoodPrice());
        holder.txtTime.setText(order.getOrderTime());


        return row;
    }

    private static class OrderHolder
    {
        ImageView userPic;
        TextView txtPrice;
        TextView txtTime;
        ImageButton btn;
    }
}
