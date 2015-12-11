package com.wheelchef.wheelchefchef.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.FloatingActionButton;
import com.wheelchef.wheelchefchef.R;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by lyk on 11/20/2015.
 */
public class HomeFragment extends Fragment {

    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View forReturn = inflater.inflate(R.layout.fragment_home, container, false);
        waveSwipeRefreshLayout = (WaveSwipeRefreshLayout) forReturn.findViewById(R.id.layout_home_swipe);


        setUpWaveRefreshLayout();
        return forReturn;
    }

    private void setUpWaveRefreshLayout(){
        waveSwipeRefreshLayout.setWaveColor(this.getActivity().getResources().getColor(R.color.color_primary));
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waveSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }


}
