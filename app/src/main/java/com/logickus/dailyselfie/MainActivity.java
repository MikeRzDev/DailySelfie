package com.logickus.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;

import android.os.Bundle;

import android.widget.FrameLayout;

import com.logickus.dailyselfie.fragment.MainFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        showSplash();

    }


    //NEEDED FOR CALLING STARTACTIVITYFORRESULT IN FRAGMENT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSplash()
    {
        getActionBar().hide();
        final FrameLayout mainFrame = (FrameLayout) findViewById(R.id.container);
        mainFrame.setBackgroundResource(R.drawable.splash);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // This method will be executed once the timer is over
                UIHelper.changeFragmentInit(R.id.container, getFragmentManager(), new MainFragment(), null);
                mainFrame.setBackgroundColor(Color.WHITE);
            }

        }, 2000);
    }



}
