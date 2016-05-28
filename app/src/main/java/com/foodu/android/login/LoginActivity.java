package com.foodu.android.login;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.foodu.android.login.BaseActivities.BaseLoginActivity;
import com.foodu.android.R;

import java.io.IOException;

/**
 * Created by ANDRO01 on 02-Feb-16.
 */
public class LoginActivity extends BaseLoginActivity implements  SurfaceHolder.Callback {
    Uri targetUri;

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean pausing = false;;
    private FragmentManager fragmentManager;
    private LoginMainFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        Log.e("lifecycle", "Onresume");


        targetUri = Uri.parse("android.resource://" +getPackageName()+ "/"+R.raw.videoplayback);

        initialisePlayer();
        playVideo();

        loginFragment = new LoginMainFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frag_container, loginFragment);
        ft.commit();


    }


    private void initialisePlayer(){
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.svBackground);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
    }

    private void playVideo() {
        pausing = false;

        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), targetUri);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        Log.e("lifecycle", "ondestroy");

        // TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        mediaPlayer.setDisplay(surfaceHolder);
      /*  //Get the dimensions of the video
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        //Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        lp.width = videoWidth;

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
   //     lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);
        lp.height =screenHeight;
        //Commit the layout parameters
        surfaceView.setLayoutParams(lp);
*/
        //Start video
        mediaPlayer.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle", "onpause");

        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onresume");

        System.out.println("ON RESUME");
        mediaPlayer.start();

      //  playVideo();


    }
}
