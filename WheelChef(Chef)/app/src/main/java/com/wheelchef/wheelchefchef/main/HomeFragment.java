package com.wheelchef.wheelchefchef.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.FloatingActionButton;
import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 11/20/2015.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View forReturn = inflater.inflate(R.layout.fragment_home, container, false);
        return forReturn;
    }


}
