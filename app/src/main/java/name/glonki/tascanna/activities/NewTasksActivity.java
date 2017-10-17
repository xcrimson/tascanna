package name.glonki.tascanna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import name.glonki.list.StandardListAdapter;
import name.glonki.rx.ObserverWithAction;
import name.glonki.rx.SimpleObserver;
import name.glonki.tascanna.TascannaApplication;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.Deleter;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.tascanna.statemachine.FSMState;
import name.glonki.tascanna.statemachine.FSMTrigger;
import name.glonki.tascanna.statemachine.FSMUtil;
import name.glonki.tascanna.util.ToastUtil;
import name.glonki.tascanna.viewactions.NewTaskViewActions;
import name.glonki.tascanna.viewsetters.NewTaskViewSetter;
import name.glonki.teamwork.TeamworkClient;
import name.glonki.teamwork.data.Person;
import name.glonki.teamwork.data.Project;
import name.glonki.teamwork.data.TaskList;
import name.glonki.teamwork.util.IdentifiableUtil;

/**
 * Created by Glonki on 14.10.2017.
 */

public class NewTasksActivity extends AppCompatActivity {

    public static final String PROJECT_PARCELABLE_KEY = "project-parcelable-key";

    private final static FSMTrigger DATA_REQUEST = new FSMTrigger("Data request");
    private final static FSMTrigger DATA_LOADED = new FSMTrigger("Data loaded");
    private final static FSMTrigger DATA_LOADING_ERROR = new FSMTrigger("Data loading error");
    private final static FSMTrigger REQUEST_INITIATED = new FSMTrigger("Request initiated");
    private final static FSMTrigger REQUEST_SUCCEEDED = new FSMTrigger("Request succeded");
    private final static FSMTrigger REQUEST_FAILED = new FSMTrigger("Request failed");

    private final static FSMState NORMAL = new FSMState("Normal");
    private final static FSMState BUSY = new FSMState("Busy");
    private final static FSMState LOADING_DATA = new FSMState("Loading data");
    private final static FSMState LOADING_DATA_ERROR = new FSMState("Loading data error");
    private final static FSMState REQUEST_IN_PROGRESS = new FSMState("Loading data error");

    private BehaviorSubject<List<TaskList>> taskListsObservable = BehaviorSubject.create();
    private BehaviorSubject<List<Person>> peopleObservable = BehaviorSubject.create();

    private Project project;
    private StandardListAdapter<NewTask> listAdapter;
    private LinearLayoutManager layoutManager;
    private TeamworkClient teamworkClient;

    private Observer<Pair<List<TaskList>, List<Person>>> taskListsPersonsObserver =
            new SimpleObserver<Pair<List<TaskList>, List<Person>>>() {
        @Override
        public void onNext(@NonNull Pair<List<TaskList>, List<Person>> pair) {

            List<Person> people = new ArrayList<>();
            people.add(Person.getEveryone(getString(R.string.everyone)));
            people.addAll(pair.second);

            taskListsObservable.onNext(pair.first);
            peopleObservable.onNext(people);

            if(pair.first.size() == 0) {
                ToastUtil.showLongToast(NewTasksActivity.this, R.string.no_task_lists);
                stateMachine.fire(DATA_LOADING_ERROR);
            } else {
                stateMachine.fire(DATA_LOADED);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            stateMachine.fire(DATA_LOADING_ERROR);
        }
    };

    private Consumer<Object> addTask = new Consumer<Object>() {
        @Override
        public void accept(Object o) throws Exception {
            addNewTask.doIt();
        }
    };

    private StateMachine<FSMState, FSMTrigger> stateMachine;

    private final int[] normalStateViews = new int[]{R.id.recycler_view, R.id.add_task};

    private Action requestData = new Action() {
        @Override
        public void doIt() {
            teamworkClient.getTaskListsAndPeople(project, taskListsPersonsObserver);
        }
    };

    private Action addNewTask = new Action() {
        @Override
        public void doIt() {
            listAdapter.addItem(NewTask.create(taskListsObservable.getValue().get(0)));
            layoutManager.scrollToPosition(listAdapter.getItemCount() - 1);
        }
    };

    private Refreshable refreshable = new Refreshable() {
        @Override
        public void refresh() {
            listAdapter.notifyDataSetChanged();
        }
    };

    private Scrollable<NewTask> scrollable = new Scrollable<NewTask>() {
        @Override
        public void scrollTo(NewTask data) {
            int position = listAdapter.getItemPosition(data);
            if(position >= 0) {
                layoutManager.scrollToPosition(position);
            }
        }
    };

    private Deleter<NewTask> deleter = new Deleter<NewTask>() {
        @Override
        public void delete(NewTask newTask) {
            listAdapter.deleteItem(newTask);
        }
    };

    private Consumer<Map<NewTask, Throwable>> addTasksObserver = new Consumer<Map<NewTask, Throwable>>() {
        @Override
        public void accept(Map<NewTask, Throwable> exceptions) throws Exception {
            if(exceptions.size() == 0) {
                stateMachine.fire(REQUEST_SUCCEEDED);
            } else {
                ToastUtil.showLongToast(NewTasksActivity.this, R.string.add_tasks_partial_success);
                List<NewTask> failedTasks = new ArrayList<>();
                failedTasks.addAll(exceptions.keySet());
                listAdapter.setData(failedTasks);
                stateMachine.fire(REQUEST_FAILED);
            }
        }
    };

    private Action addTasks = new Action() {
        @Override
        public void doIt() {
            teamworkClient.addTasks(listAdapter.getData(), addTasksObserver);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tasks);

        project = getProject(getIntent(), savedInstanceState);
        if(project == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        teamworkClient = TascannaApplication.getInstance().getTeamworkClient();

        StateMachineConfig<FSMState, FSMTrigger> stateMachineConfig = new StateMachineConfig<>();

        stateMachineConfig.configure(BUSY)
                .onEntry(FSMUtil.setViewVisible(this, R.id.progress_bar, true))
                .onExit(FSMUtil.setViewVisible(this, R.id.progress_bar, false));

        stateMachineConfig.configure(LOADING_DATA_ERROR)
                .onEntry(FSMUtil.setViewVisible(this, R.id.error_loading_data, true))
                .onExit(FSMUtil.setViewVisible(this, R.id.error_loading_data, false))
                .permit(DATA_REQUEST, LOADING_DATA);

        stateMachineConfig.configure(LOADING_DATA)
                .substateOf(BUSY)
                .onEntryFrom(DATA_REQUEST, requestData)
                .permit(DATA_LOADED, NORMAL)
                .permit(DATA_LOADING_ERROR, LOADING_DATA_ERROR);

        stateMachineConfig.configure(NORMAL)
                .onEntry(FSMUtil.setViewsVisible(this, normalStateViews, true))
                .onExit(FSMUtil.setViewsVisible(this, normalStateViews, false))
                .onEntryFrom(REQUEST_SUCCEEDED, FSMUtil.finishActivityWithResult(this, RESULT_OK))
                .onEntryFrom(DATA_LOADED, addNewTask)
                .permit(REQUEST_INITIATED, REQUEST_IN_PROGRESS)
                .permit(DATA_REQUEST, LOADING_DATA);

        stateMachineConfig.configure(REQUEST_IN_PROGRESS)
                .onEntry(addTasks)
                .substateOf(BUSY)
                .permit(REQUEST_SUCCEEDED, NORMAL)
                .permit(REQUEST_FAILED, NORMAL);

        stateMachine = new StateMachine<>(LOADING_DATA, stateMachineConfig);

        Function<List<Person>, Map<Integer, Person>> peopleToMap = new Function<List<Person>, Map<Integer, Person>>() {
            @Override
            public Map<Integer, Person> apply(@NonNull List<Person> persons) throws Exception {
                return IdentifiableUtil.getIdMap(persons);
            }
        };

        BehaviorSubject<Map<Integer, Person>> peopleMap = BehaviorSubject.create();
        peopleObservable.map(peopleToMap).subscribe(peopleMap);

        NewTaskViewSetter viewSetter = new NewTaskViewSetter(taskListsObservable, peopleMap);
        NewTaskViewActions viewActions = NewTaskViewActions.create(this, refreshable, scrollable, deleter,
                peopleObservable, taskListsObservable);

        listAdapter = new StandardListAdapter<>(viewSetter, viewActions);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        FloatingActionButton addTaskButton = findViewById(R.id.add_task);
        RxView.clicks(addTaskButton).subscribe(addTask);

        View dataReload = findViewById(R.id.data_reload_button);
        RxView.clicks(dataReload).subscribe(new ObserverWithAction(FSMUtil.fireTrigger(stateMachine, DATA_REQUEST)));

        String title = String.format(getString(R.string.new_tasks_toolbar_title), project.getName());
        Toolbar toolbar = findViewById(R.id.new_tasks_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        requestData.doIt();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_submit_tasks:
                if(stateMachine.isInState(NORMAL)) {
                    stateMachine.fire(REQUEST_INITIATED);
                } else {
                    ToastUtil.showLongToast(this, R.string.cant_submit_tasks);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(stateMachine.isInState(BUSY)) {
            ToastUtil.showLongToast(this, R.string.wait_please);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private Project getProject(Intent intent, Bundle savedInstanceState) {
        Bundle intentExtras = intent==null? null : intent.getExtras();
        Bundle dataBundle = intentExtras==null? savedInstanceState : intentExtras;
        return dataBundle.getParcelable(PROJECT_PARCELABLE_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PROJECT_PARCELABLE_KEY, project);
        super.onSaveInstanceState(outState);
    }

}
