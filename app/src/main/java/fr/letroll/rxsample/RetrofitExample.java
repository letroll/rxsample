package fr.letroll.rxsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.letroll.rxsample.retrofitexample.ApiManager;
import fr.letroll.rxsample.retrofitexample.GitHubMember;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RetrofitExample extends Activity{

    /** VIEWS **/
    @InjectView(R.id.tv_retrofit)
    TextView tv_retrofit;

    /** VARIABLES **/
    private static final String[] GITHUB_MEMBERS = new String[]{"mojombo", "JakeWharton", "mattt", "letroll"};

    /** OBJECTS **/
    private Subscription mSubscription;
    private Observer<? super String> retrofitObserver= new Observer<String>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable throwable) {
            Toast.makeText(RetrofitExample.this, "Error:"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(String s) {
            tv_retrofit.setText(String.format(Locale.US, "%s\n%s", s, tv_retrofit.getText()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_example);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bt_start)
    void start(){
        mSubscription = getFollowersObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retrofitObserver);
    }

    private Observable getFollowersObservable() {
        return Observable.from(GITHUB_MEMBERS)
                .flatMap(new Func1<String, Observable<GitHubMember>>() {
                    @Override
                    public Observable<GitHubMember> call(String s) {
                        return ApiManager.getGitHubMember(s);
                    }
                })
                .map(new Func1<GitHubMember, String>() {
                    @Override
                    public String call(GitHubMember gitHubMember) {
                        return gitHubMember.toString();
                    }
                })
                .reduce(new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        return s + "\n" + s2;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        mSubscription.unsubscribe();
    }

}
