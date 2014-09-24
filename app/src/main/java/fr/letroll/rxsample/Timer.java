package fr.letroll.rxsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class Timer extends Activity {

    /** VARIABLES **/
    private Subscription subs;

    /** VIEWS **/
    @InjectView(R.id.tv_timer)
    TextView tv_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.inject(this);

        changeTextAfterFiveSeconds();
    }

    private void changeTextAfterFiveSeconds() {
        subs = Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                tv_timer.setText("Boom");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subs != null) subs.unsubscribe();
        ButterKnife.reset(this);
    }
}
