package com.tera.rxthreaddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by NghiaNT on 5/20/16.
 */
public class FetchDataActivity extends BaseActivity {

    String TAG = FetchDataActivity.class.getSimpleName();
    String url_Google = "https://www.google.com/";
    String url_Yahoo = "https://yahoo.com.vn";
    TextView textViewAll, textViewG, textViewY;
    Random random = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_web_data);
        textViewAll = (TextView) findViewById(R.id.textViewAll);
        textViewG = (TextView) findViewById(R.id.textViewG);
        textViewY = (TextView) findViewById(R.id.textViewY);

        textViewTime = (TextView) findViewById(R.id.textViewClockMain);
        initObservable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        coundDownTime();
    }

    void initObservable() {
        Observable<String> fetchFromGoogle = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                fetDataFrom(url_Google, 2, subscriber);
            }
        });
        Observable<String> fetchFromYahoo = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                fetDataFrom(url_Yahoo, 11, subscriber);
            }
        });
        fetchFromGoogle
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /*.subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        textViewG.append("COMPLETED - GOOGLE");
                    }

                    @Override
                    public void onError(Throwable e) {
                        textViewG.append("ERROR - GOOGLE");
                    }

                    @Override
                    public void onNext(String s) {
                        textViewG.append(s);
                    }
                })*/;

        fetchFromYahoo
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /*.subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        textViewY.append(" COMPLETED - YAHOO");
                    }

                    @Override
                    public void onError(Throwable e) {
                        textViewY.append(" ERROR - YAHOO");
                    }

                    @Override
                    public void onNext(String s) {
                        textViewY.append(s);
                    }
                })*/;
        Observable<String> zipped = Observable.zip(fetchFromGoogle, fetchFromYahoo, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + "\\n " + s2;
            }
        });
        zipped.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> textViewAll.setText(x));

       // Observable<String> concat = Observable.concat(fetchFromGoogle, fetchFromYahoo);
//        concat.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(x -> textViewAll.setText("concat " + x));
    }

    public void fetDataFrom(String url, int delta, Subscriber<? super String> subscriber) {

        StringBuilder result = new StringBuilder("URL-content://" + url + "\\n");

        try {


            int size = 20;//random.nextInt(20) + delta;
            Log.d(TAG, "fetDataFrom url=" + url + " Data size = " + size);
            result.append(" size=" + size + " ");
            subscriber.onNext(result.toString());


            for (int i = 0; i < size; i++) {
                result.setLength(0);
                Log.d(TAG, url + " iterate i=" + i);
                for (int j = 0; j < 10; j++) {
                    result.append(i);
                }
                subscriber.onNext(result.toString());

                result.append(" ");
                Thread.sleep(1000);
            }

            subscriber.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestDataFromURL(String url, OnFetDataResponse listener) {
        StringBuilder result = new StringBuilder("URL-content://" + url + "\\n");

        try {

            int rani = random.nextInt(20);
            for (int i = 0; i < rani; i++) {
                for (int j = 0; j < 20; j++) {
                    result.append(i);
                }
                Thread.sleep(1000);
                result.append("\\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listener.onResponse(result.toString());
    }

    public interface OnFetDataResponse {
        void onResponse(String data);
    }
}
