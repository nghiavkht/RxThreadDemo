package com.tera.rxthreaddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    private int timeSS = 0;
    private int timeHH = 0;

    TextView textViewTime;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ArrayList<TextView> arrayListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - start");
        setContentView(R.layout.activity_main);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        arrayListTextView = new ArrayList<>();
        arrayListTextView.add(textView1);
        arrayListTextView.add(textView2);
        arrayListTextView.add(textView3);
        arrayListTextView.add(textView4);
        textViewTime.setText("00:00");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - start");
        coundDownTime();
        Observable<Integer> myObservable1 = Observable.from(new Integer[]{1, 2, 3, 4});
        /*Observable<Integer> myObservable2 = Observable.create(new AsyncOnSubscribe<String, Integer>() {
            @Override
            protected String generateState() {
                return null;
            }

            @Override
            protected String next(String state, long requested, Observer<Observable<? extends Integer>> observer) {
                return null;
            }
        });*/

        Observer<Integer> myObserver = new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext integer= " + integer);
                //arrayListTextView.get(integer - 1).setText(integer);
                countSum(integer);
            }
        };

        myObservable1
                //.map(x -> x+1)
                //.filter(x -> x % 2 == 0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myObserver);
    }

    public void countSum(int n) {
        Observable<Integer> observable = Observable.range(n / 2, n);
        observable
                .reduce((a, b) ->{
                    return a * b;
                })
                .delaySubscription(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                            Log.d(TAG, "count Sum of " + n + " = " + x);
                            arrayListTextView.get(n - 1).append(" sum=" + x);
                        }
                );
    }

    public void coundDownTime() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                while(true){
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
                        strTimeSS = "0"+strTimeSS;
                    if (timeHH < 10)
                        strTimeHH = "0"+strTimeHH;
                    subscriber.onNext(strTimeHH + ":" + strTimeSS);
                }

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(str-> {
                    textViewTime.setText(str);
                });
    }
}