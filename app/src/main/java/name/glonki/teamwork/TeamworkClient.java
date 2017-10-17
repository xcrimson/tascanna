package name.glonki.teamwork;

import android.util.Pair;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import name.glonki.rx.ArrayContainerToList;
import name.glonki.rx.RxUtil;
import name.glonki.rx.ToPair;
import name.glonki.tascanna.data.NewTask;
import name.glonki.teamwork.data.Person;
import name.glonki.teamwork.data.Project;
import name.glonki.teamwork.data.TaskList;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Glonki on 14.10.2017.
 */

public class TeamworkClient {

    private final static String API_BASE_URL = "https://yat.teamwork.com";
    private final static String API_USERNAME = "twp_TEbBXGCnvl2HfvXWfkLUlzx92e3T";
    private final static String API_PWD = "X";

    private final TeamworkAPI teamworkAPI;

    private TeamworkClient(TeamworkAPI teamworkAPI) {
        this.teamworkAPI = teamworkAPI;
    }

    public static TeamworkClient create() {
        return create(API_BASE_URL, API_USERNAME, API_PWD);
    }

    public static TeamworkClient create(String baseUrl, final String username, final String password) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(username, password));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        TeamworkAPI teamworkAPI = retrofit.create(TeamworkAPI.class);

        return new TeamworkClient(teamworkAPI);
    }

    public void getProjects(Observer<List<Project>> observer) {
        teamworkAPI.getProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ArrayContainerToList<Project>())
                .subscribe(observer);
    }

    public void addTasks(final List<NewTask> tasks, final Consumer<Map<NewTask, Throwable>> resultConsumer) {

        Consumer<NewTask> job = new Consumer<NewTask>() {
            @Override
            public void accept(NewTask task) throws Exception {
                Response<Void> response = teamworkAPI.addTaskSync(task.getTaskList().getId(), task.getTaskToAdd()).execute();
                if(!response.isSuccessful()) {
                    String errorMessage = response.errorBody().string();
                    throw new InvalidParameterException(errorMessage);
                }
            }
        };

        RxUtil.processCollectionSequenciallyAsync(Schedulers.io(), tasks, job, resultConsumer);

    }

    public void getPeople(Observer<List<Person>> observer) {
        getListPersonObservable().subscribe(observer);
    }

    public void getTaskLists(Project project, Observer<List<TaskList>> observer) {
        getListTaskListObservable(project).subscribe(observer);
    }

    private Observable<List<Person>> getListPersonObservable() {
        return teamworkAPI.getPeople()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ArrayContainerToList<Person>());
    }

    private Observable<List<TaskList>> getListTaskListObservable(Project project) {
        return teamworkAPI.getTaskLists(project.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ArrayContainerToList<TaskList>());
    }

    public void getTaskListsAndPeople(Project project, Observer<Pair<List<TaskList>, List<Person>>> observer) {
        Observable.zip(getListTaskListObservable(project), getListPersonObservable(),
                new ToPair<List<TaskList>, List<Person>>()).subscribe(observer);
    }



}
