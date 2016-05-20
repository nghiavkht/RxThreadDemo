package com.tera.rxthreaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    String TAG = MainActivity.class.getSimpleName();
    private int timeSS = 0;
    private int timeHH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - start");
        setContentView(R.layout.activity_main);
        textViewTime = (TextView) findViewById(R.id.textViewClockMain);
        findViewById(R.id.buttonMain1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ClockDataActivity.class));
            }
        });

        findViewById(R.id.buttonMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, FetchDataActivity.class));
            }
        });


        textViewTime.setText("--:--");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - start");
        coundDownTime();
    }
}
