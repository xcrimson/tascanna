package name.glonki.tascanna.activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.RequestManager;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import name.glonki.list.StandardListAdapter;
import name.glonki.list.ViewAction;
import name.glonki.rx.ObserverWithAction;
import name.glonki.rx.SimpleObserver;
import name.glonki.tascanna.TascannaApplication;
import name.glonki.tascanna.Navigator;
import name.glonki.tascanna.R;
import name.glonki.tascanna.statemachine.FSMState;
import name.glonki.tascanna.statemachine.FSMTrigger;
import name.glonki.tascanna.statemachine.FSMUtil;
import name.glonki.tascanna.util.ToastUtil;
import name.glonki.tascanna.viewactions.ProjectViewActions;
import name.glonki.tascanna.viewsetters.ProjectViewSetter;
import name.glonki.teamwork.TeamworkClient;
import name.glonki.teamwork.data.Project;

public class ProjectsActivity extends AppCompatActivity {

    private final static FSMTrigger DATA_REQUEST = new FSMTrigger("Data request");
    private final static FSMTrigger DATA_LOADED = new FSMTrigger("Data loaded");
    private final static FSMTrigger DATA_LOADING_ERROR = new FSMTrigger("Data loading error");

    private final static FSMState NORMAL = new FSMState("Normal");
    private final static FSMState LOADING_DATA = new FSMState("Loading data");
    private final static FSMState LOADING_DATA_ERROR = new FSMState("Loading data error");

    private Observer<List<Project>> projectsObserver = new SimpleObserver<List<Project>>() {

        @Override
        public void onNext(@NonNull List<Project> projects) {
            projectsAdapter.setData(projects);
            stateMachine.fire(DATA_LOADED);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            stateMachine.fire(DATA_LOADING_ERROR);
        }

    };

    private ViewAction<Project, Object> newTasksClickAction = new ViewAction<Project, Object>() {
        @Override
        public int getViewId() {
            return R.id.new_tasks_button;
        }

        @Override
        public void accept(Pair<Project, Object> pair) throws Exception {
            Navigator.openNewTasksActivity(ProjectsActivity.this, pair.first);
        }
    };

    private TeamworkClient teamworkClient;
    private StandardListAdapter<Project> projectsAdapter;
    private ProjectViewActions projectViewActions;

    private StateMachine<FSMState, FSMTrigger> stateMachine;

    private Action requestData = new Action() {
        @Override
        public void doIt() {
            teamworkClient.getProjects(projectsObserver);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        teamworkClient = TascannaApplication.getInstance().getTeamworkClient();

        StateMachineConfig<FSMState, FSMTrigger> stateMachineConfig = new StateMachineConfig<>();

        stateMachineConfig.configure(LOADING_DATA)
                .onEntry(FSMUtil.setViewVisible(this, R.id.progress_bar, true))
                .onExit(FSMUtil.setViewVisible(this, R.id.progress_bar, false))
                .onEntryFrom(DATA_REQUEST, requestData)
                .permit(DATA_LOADED, NORMAL)
                .permit(DATA_LOADING_ERROR, LOADING_DATA_ERROR);

        stateMachineConfig.configure(NORMAL)
                .onEntry(FSMUtil.setViewVisible(this, R.id.recycler_view, true))
                .onExit(FSMUtil.setViewVisible(this, R.id.recycler_view, false))
                .permit(DATA_REQUEST, LOADING_DATA);

        stateMachineConfig.configure(LOADING_DATA_ERROR)
                .onEntry(FSMUtil.setViewVisible(this, R.id.error_loading_data, true))
                .onExit(FSMUtil.setViewVisible(this, R.id.error_loading_data, false))
                .permit(DATA_REQUEST, LOADING_DATA);

        stateMachine = new StateMachine<>(LOADING_DATA, stateMachineConfig);

        View dataReload = findViewById(R.id.data_reload_button);
        RxView.clicks(dataReload).subscribe(new ObserverWithAction(FSMUtil.fireTrigger(stateMachine, DATA_REQUEST)));

        RequestManager picRequestManger = TascannaApplication.getInstance().getGlideRequestManager();
        projectViewActions = ProjectViewActions.create(newTasksClickAction);
        projectsAdapter = new StandardListAdapter<>(new ProjectViewSetter(picRequestManger),
                projectViewActions);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(projectsAdapter);

        Toolbar toolbar = findViewById(R.id.projects_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.my_projects_toolbar_title);

        requestData.doIt();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.projects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_reload_projects:
                stateMachine.fire(DATA_REQUEST);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Navigator.NEW_TASKS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ToastUtil.showLongToast(this, R.string.tasks_added_successfully);
            }
        }
    }

}
