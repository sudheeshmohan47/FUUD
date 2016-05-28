package com.foodu.android.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.foodu.android.R;
import com.foodu.android.login.BaseActivities.BaseLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ANDRO01 on 02-Feb-16.
 */
public class LoginActivity extends BaseLoginActivity   {

    @BindView(R.id.frag_container)
    FrameLayout frameLayoutContainer;

    @BindView(R.id.videoBackground)
    VideoView videoView;

    Uri targetUri;
    private FragmentManager fragmentManager;
    private WelcomeFragment loginFragment;
    boolean isPaused = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);



        Log.e("lifecycle", "Onresume");

        targetUri = Uri.parse("android.resource://" +getPackageName()+ "/"+R.raw.videoplayback);

        //Setting MediaController and URI, then starting the videoView
        videoView.setVideoURI(targetUri);
        videoView.setMediaController(null);
        videoView.start();

        loginFragment = new WelcomeFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frag_container, loginFragment);
        ft.commit();

       // RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(device_width, device_height);
       // lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //lp.alignWithParent = false;
       // frameLayoutContainer.setLayoutParams(lp);

    }




    @Override
    protected void onDestroy() {
        Log.e("lifecycle", "ondestroy");

        // TODO Auto-generated method stub
        super.onDestroy();
       // mediaPlayer.release();
    }



    @Override
    protected void onPause() {
        super.onPause();
               Log.e("lifecycle", "onpause");

        videoView.pause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onresume");

        System.out.println("ON RESUME");

      //  playVideo();
        if(isPaused) {
            videoView.start();
            isPaused = false;
        }


    }


}
