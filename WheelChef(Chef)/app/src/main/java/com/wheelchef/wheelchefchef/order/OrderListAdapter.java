package com.wheelchef.wheelchefchef.order;

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
            holder.txtTime = (TextView)row.findViewById(R.id.order_list_orderTime);
            holder.btn = (ImageButton)row.findViewById(R.id.btn_view_order_detail);
            holder.wholeRow = (RelativeLayout) row.findViewById(R.id.row_order_list);

            row.setTag(holder);
        }
        else
        {
            holder = (OrderHolder)row.getTag();
        }

        final OrderModel order = data.get(position);
        holder.txtPrice.setText("S$ " + order.getFoodPrice());
        holder.txtTime.setText(order.getOrderTime());

        if(order.getCutomerName().equals("lyk"))
            holder.userPic.setImageResource(R.drawable.user_lyk);
        else if (order.getCutomerName().equals("mhq6634"))
            holder.userPic.setImageResource(R.drawable.user_mhq);


        if(order.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_ACCEPTED)){
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_green));
        } else if (order.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_REJECTED)){
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
        } else {
            holder.wholeRow.setBackgroundColor(context.getResources().getColor(R.color.color_accent));
        }


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewOrderDetailActivity.class);
                intent.putExtra(ViewOrderDetailActivity.ORDER_MODEL_OBJ,order);

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });

        return row;
    }

    private static class OrderHolder
    {
        ImageView userPic;
        TextView txtPrice;
        TextView txtTime;
        ImageButton btn;
        RelativeLayout wholeRow;
    }
}
