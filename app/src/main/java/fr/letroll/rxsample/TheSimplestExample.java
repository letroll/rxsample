package fr.letroll.rxsample;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
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
    private Observable<Time> currentTime;

    /** VIEWS **/
    @InjectView(R.id.tv_simplest_example)
    TextView tv_simplest_example;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_simplest_example);
        ButterKnife.inject(this);
        currentTime=getTimeObservable();
        ObserveTime();
    }

    @Override
    protected void onDestroy() {
        currentTime.unsubscribeOn(AndroidSchedulers.mainThread());
        super.onDestroy();
    }

    private Observable<Time> getTimeObservable(){
        Time time = new Time(Time.getCurrentTimezone());
        return Observable.just(time, AndroidSchedulers.mainThread());
    }

    private void ObserveTime(){
        currentTime.subscribe(new Action1<Time>() {
            @Override
            public void call(Time time) {
                time.setToNow();
                tv_simplest_example.setText(time.format("%k:%M:%S"));
                ObserveTime();
            }
        });
    }

}
