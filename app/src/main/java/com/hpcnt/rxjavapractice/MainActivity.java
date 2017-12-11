package com.hpcnt.rxjavapractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> mObservable = Observable.just("Hello Rx!");

        Consumer<String> onNextAction = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("onNextAction", s);
            }
        };

        Action onCompleteAction = new Action() {
            @Override
            public void run() throws Exception {
                Log.e("onCompleteAction", "onComplete!!");
            }
        };

        Consumer<Throwable> onErrorAction = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.e("onErrorAction", throwable.getMessage());
            }
        };

        mObservable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }
}
