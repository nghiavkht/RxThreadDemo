package com.tera.rxthreaddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by NghiaNT on 5/20/16.
 */
public class BaseActivity extends AppCompatActivity {
    protected TextView textViewTime;
    protected int timeSS = 0;
    protected int timeHH = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void coundDownTime() {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timeSS++;
                    if (timeSS == 60) {
                        timeHH++;
                        timeSS = 0;
                    }
                    String strTimeSS = "" + timeSS;
                    String strTimeHH = "" + timeHH;
                    if (timeSS < 10)
                        strTimeSS = "0" + strTimeSS;
                    if (timeHH < 10)
                        strTimeHH = "0" + strTimeHH;
                    subscriber.onNext(strTimeHH + ":" + strTimeSS);
                }

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(str -> {
                    if (textViewTime != null)
                        textViewTime.setText(str);
                });
    }
}
