package fr.letroll.rxsample.retrofitexample;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by jquievreux on 17/07/2014.
 */
public class ApiManager {

    /**
     * VARIABLES *
     */
    public final static String TAG = "ApiManager";

    /** OBJECTS **/

    private interface ApiManagerService {
        @GET("/users/{username}")
        GitHubMember getMember(@Path("username") String username);
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.github.com")
            .build();

    private static final ApiManagerService apiManager = restAdapter.create(ApiManagerService.class);

    public static Observable<GitHubMember> getGitHubMember(final String username) {
        return Observable.create(new Observable.OnSubscribeFunc<GitHubMember>() {
            @Override
            public Subscription onSubscribe(Observer<? super GitHubMember> observer) {
                try {
                    GitHubMember member = apiManager.getMember(username);
                    observer.onNext(member);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
}