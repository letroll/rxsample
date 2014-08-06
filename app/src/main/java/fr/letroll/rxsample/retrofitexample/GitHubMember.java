package fr.letroll.rxsample.retrofitexample;

import java.util.Locale;

/**
 * Created by jquievreux on 17/07/2014.
 */
public class GitHubMember {

    /**
     * VARIABLES *
     */
    public final static String TAG = "GitHubMember";
    public String login;
    public int followers;

    /** OBJECTS **/

    @Override
    public String toString() {
        return String.format(Locale.US, "%s: %d followers", login, followers);
    }
}
