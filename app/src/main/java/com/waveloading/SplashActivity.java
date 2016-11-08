package com.waveloading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.waveloading.splashview.SplashView;

/**
 * Created by lichengcai on 2016/11/7.
 */

public class SplashActivity extends Activity {
    private SplashView mSplashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashView = (SplashView) findViewById(R.id.splashView);

    }

    public void start(View view) {
        Toast.makeText(this,"start",Toast.LENGTH_SHORT).show();
        mSplashView.start();
    }

    public void circle(View view) {
        mSplashView.circleTest();
    }
}
