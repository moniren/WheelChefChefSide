package com.wheelchef.wheelchefchef.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 11/14/2015.
 */
public class CustomMenuFragment extends MenuFragment {

//    private ImageView ivMenuUserProfilePhoto;
    private TextView usernameText;
    private NavigationView navigationView;

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
        if(navigationView == null || usernameText == null){
            this.getActivity().finish();
        }
        //ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        return  setupReveal(view) ;
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
