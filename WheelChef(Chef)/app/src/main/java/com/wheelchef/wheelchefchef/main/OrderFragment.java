package com.wheelchef.wheelchefchef.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;
import com.wheelchef.wheelchefchef.order.OrderListAdapter;
import com.wheelchef.wheelchefchef.registerlogin.SessionManager;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends android.support.v4.app.Fragment {


    private ListView listView;
    private Button btnViewDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View forReturn = inflater.inflate(R.layout.fragment_order, container, false);

        // Get ListView object from xml
        listView = (ListView) forReturn.findViewById(R.id.order_list);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Username: mhq6634   \nTime:2015.12.11 12:15pm",
                "Username: lyk       \nTime:2015.12.11 12:25pm"
        };

        ArrayList<OrderModel> dataList = new ArrayList<>();



        OrderModel m1 = new OrderModel(1,"mhq6634","blk 258 #7-12-222 Jurong West 91","BUNJYH4858","3.3","safd666","2015.12.11 12:15pm",
                PrefUtil.getStringPreference(SessionManager.USERNAME,this.getActivity()));

        OrderModel m2 = new OrderModel(1,"lyk","blk 555 #7-5-222 Jurong West 91","aasd45455f","5.5","asdfas888","2015.12.11 12:25pm",
                PrefUtil.getStringPreference(SessionManager.USERNAME,this.getActivity()));

        dataList.add(m1);
        dataList.add(m2);

        OrderListAdapter adapter = new OrderListAdapter(this.getActivity(),R.layout.row_order_list,dataList);

        listView.setAdapter(adapter);
        return forReturn;
    }



}
