package name.glonki.tascanna;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import name.glonki.teamwork.TeamworkClient;

/**
 * Created by Glonki on 14.10.2017.
 */

public class TascannaApplication extends Application {

    private static TascannaApplication application;

    private TeamworkClient teamworkClient;
    private RequestManager glideRequestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        teamworkClient = TeamworkClient.create();
        glideRequestManager = Glide.with(this);
    }

    public static TascannaApplication getInstance() {
        return application;
    }

    public TeamworkClient getTeamworkClient() {
        return teamworkClient;
    }

    public RequestManager getGlideRequestManager() {
        return glideRequestManager;
    }

}
