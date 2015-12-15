package com.wheelchef.wheelchefchef.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;
import com.wheelchef.wheelchefchef.order.OrderListAdapter;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import java.util.ArrayList;


public class OrderFragment extends android.support.v4.app.Fragment {


    private ListView listView;
    private Button btnViewDetail;
    private static OrderModel m1 = null;
    private static OrderModel m2 = null;

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

        ArrayList<String> dishList1 = new ArrayList<>();
        ArrayList<String> dishList2 = new ArrayList<>();

        ArrayList<Integer> amountList1 = new ArrayList<>();
        ArrayList<Integer> amountList2 = new ArrayList<>();


        dishList1.add("Gong Bao Ji Ding");
        dishList1.add("Shui Zhu Niu Rou");
        amountList1.add(5);
        amountList1.add(1);

        dishList2.add("Prata");
        dishList2.add("Curry Chicken");
        amountList2.add(10);
        amountList2.add(3);


        if(m1==null) {
            m1 = new OrderModel(amountList1, "mhq6634", "blk 258 #7-12-222 Jurong West 91", dishList1, "19.8", "safd666", "2015.12.11 12:15pm",
                    PrefUtil.getStringPreference(SessionManager.USERNAME, this.getActivity()), "");
        }
        if(m2==null){
            m2 = new OrderModel(amountList2,"lyk","blk 555 #7-5-222 Jurong West 91",dishList2,"42.9","asdfas888","2015.12.11 12:25pm",
                    PrefUtil.getStringPreference(SessionManager.USERNAME,this.getActivity()),"");
        }

        dataList.add(m1);
        dataList.add(m2);

        OrderListAdapter adapter = new OrderListAdapter(this.getActivity(),R.layout.row_order_list,dataList);

        listView.setAdapter(adapter);
        return forReturn;
    }

    public static void changeStatus(String userId, String status){
        if(m1.getCutomerName().equals(userId))
            m1.setAcceptanceStatus(status);
        else if(m2.getCutomerName().equals(userId))
            m2.setAcceptanceStatus(status);
    }



}
