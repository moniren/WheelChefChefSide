package com.wheelchef.wheelchefchef.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.pkmmte.view.CircularImageView;
import com.rey.material.widget.Switch;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.account.EditAccountActivity;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.utils.BitmapUtil;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

/**
 * Created by lyk on 11/14/2015.
 */
public class NavigationMenuFragment extends MenuFragment {

//    private ImageView ivMenuUserProfilePhoto;
    private TextView usernameText;
    private TextView tvAvailability;
    private NavigationView navigationView;
    private CircularImageView civProfilePhoto;
    private ImageView ivBg;
    private Switch swAvailability;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);

        usernameText = (TextView) view.findViewById(R.id.username_text_header);

        tvAvailability = (TextView) view.findViewById(R.id.textview_availability);

        civProfilePhoto = (CircularImageView) view.findViewById(R.id.profile_image);

        swAvailability = (Switch) view.findViewById(R.id.switch_availability);

        ivBg = (ImageView) view.findViewById(R.id.imageview_header_bg);

        if(navigationView == null || usernameText == null){
            this.getActivity().finish();
        }
        //ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        setUpAccountInfo();
        setUpSwitch();
        return  setupReveal(view) ;
    }

    private void setUpAccountInfo(){
        String username = PrefUtil.getStringPreference(SessionManager.USERNAME, getActivity());
        usernameText.setText(username);

        civProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationMenuFragment.this.getActivity(), EditAccountActivity.class);
                NavigationMenuFragment.this.startActivity(intent);
            }
        });
    }

    private void setUpSwitch(){
        //// TODO: add logic for retrieving the status
        tvAvailability.setText(NavigationMenuFragment.this.getActivity().getResources().getString(R.string.available));

        swAvailability.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked){
                    BitmapUtil.setUnlocked(ivBg);
                    BitmapUtil.setUnlocked(civProfilePhoto);
                    tvAvailability.setText(NavigationMenuFragment.this.getActivity().getResources().getString(R.string.available));
                }else{
                    BitmapUtil.setLocked(ivBg);
                    BitmapUtil.setLocked(civProfilePhoto);
                    tvAvailability.setText(NavigationMenuFragment.this.getActivity().getResources().getString(R.string.busy));
                }
            }
        });
    }

    public NavigationView getNavigationView(){
        return navigationView;
    }

    public TextView getUsernameText(){
        return usernameText;
    }

//    private void setupHeader() {

//        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
//        String profilePhoto = getResources().getString(R.string.user_profile_photo);
//        Picasso.with(getActivity())
//                .load(profilePhoto)
//                .placeholder(R.drawable.img_circle_placeholder)
//                .resize(avatarSize, avatarSize)
//                .centerCrop()
//                .transform(new CircleTransformation())
//                .into(ivMenuUserProfilePhoto);
//    }

}
