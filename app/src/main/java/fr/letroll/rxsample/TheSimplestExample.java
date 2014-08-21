package fr.letroll.rxsample;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jquievreux on 16/07/2014.
 */
public class TheSimplestExample extends Activity{

    /**
     * VARIABLES *
     */
    public final static String TAG = "TheSimplestExample";

    /** OBJECTS **/
    private Observable<Long> currentTime;
    private Subscription subscription;
    private Time time;

    /** VIEWS **/
    @InjectView(R.id.tv_simplest_example)
    TextView tv_simplest_example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_simplest_example);
        ButterKnife.inject(this);

        time = new Time(Time.getCurrentTimezone());
        ObserveTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        ButterKnife.reset(this);
    }

    private Observable<Long> getTimeObservable(){
        return Observable.interval(1, TimeUnit.SECONDS,AndroidSchedulers.mainThread());
    }

    private void ObserveTime(){
        currentTime=getTimeObservable();
        subscription= AndroidObservable.bindActivity(this, currentTime).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                time.setToNow();
                if(tv_simplest_example!=null)
                    tv_simplest_example.setText(time.format("%k:%M:%S"));
            }
        });
    }

}
