package com.waveloading;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class MainActivity extends Activity {
    private WaveLoadingView loadingView;
    private SeekBar mSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingView = (WaveLoadingView) findViewById(R.id.waveLoading);

        loadingView.setSpeed(0.1);
        loadingView.setPaintColor(getResources().getColor(R.color.colorDefault));
        loadingView.setBgBitmap(R.drawable.koolearn);
        loadingView.setFullListener(new WaveLoadingView.FullListener() {
            @Override
            public void full() {
                Log.d("full", "full listener---");
            }
        });
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingView.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
