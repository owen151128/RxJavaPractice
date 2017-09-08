package com.hpcnt.rxjavapractice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hpcnt.rxjavapractice.databinding.ActivityMainBinding;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Flowable.just("Hello! Rx!").subscribe(new Consumer<String>() {  //before
            @Override
            public void accept(String s) throws Exception {
                Log.e("DEBUG", s);
            }
        });

        Flowable.just("Hello! Rx!").subscribe(s -> Log.e("DEBUG", s));  //after

        Flowable.fromCallable(new Callable<String>() {  //before
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "RxJava 5s";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(s -> Log.e("DEBUG", s), throwable -> throwable.printStackTrace());

        Flowable.fromCallable(() -> {   //after
            Thread.sleep(5000);
            return "RxJava 5s";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(s -> Log.e("DEBUG", s), Throwable::printStackTrace);

        Flowable<String> source = Flowable.fromCallable(() -> { //before
            Thread.sleep(10000);
            return "RxJava 10s";
        });

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());
        Flowable<String> showFlowable = runBackground.observeOn(Schedulers.single());

        showFlowable.subscribe(s -> Log.e("DEBUG", s), Throwable::printStackTrace);

        Flowable.fromCallable(() -> {   //after
            Thread.sleep(10000);
            return "RxJava 10s";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(s -> Log.e("DEBUG", s), Throwable::printStackTrace);

        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> v * v)
                .blockingSubscribe(integer -> Log.e("Multiply", integer + ""));

        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(integer -> integer * integer)
                .sequential()
                .blockingSubscribe(integer -> Log.e("Multiply", integer + ""));

        mainBinding.mainButton.setText(R.string.button_text);
        mainBinding.mainButton.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "버튼 눌림", Toast.LENGTH_SHORT).show());
    }
}