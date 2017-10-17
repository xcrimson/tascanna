package name.glonki.tascanna.viewactions;

import android.app.Activity;
import android.util.Pair;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import name.glonki.list.ViewDialogAction;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.tascanna.util.DialogUtil;
import name.glonki.teamwork.data.TaskList;
import name.glonki.teamwork.util.NamedUtil;

/**
 * Created by Glonki on 17.10.2017.
 */

public class TaskListPickAction implements ViewDialogAction<NewTask, Object, Integer> {

    private final Activity activity;
    private final Refreshable refreshable;
    private final Scrollable<NewTask> scrollable;
    private final BehaviorSubject<List<TaskList>> taskLists;

    public TaskListPickAction(Activity activity, Refreshable refreshable,
                              Scrollable<NewTask> scrollable,
                              BehaviorSubject<List<TaskList>> taskLists) {
        this.activity = activity;
        this.refreshable = refreshable;
        this.scrollable = scrollable;
        this.taskLists = taskLists;
    }

    @Override
    public Consumer<Pair<NewTask, Integer>> getDialogResultConsumer() {
        return new Consumer<Pair<NewTask, Integer>>() {
            @Override
            public void accept(Pair<NewTask, Integer> pair) {
                pair.first.setTaskList(taskLists.getValue().get(pair.second));
            }
        };
    }

    @Override
    public int getViewId() {
        return R.id.task_list;
    }

    @Override
    public void accept(Pair<NewTask, Object> pair) throws Exception {
        DialogUtil.getListSelectorDialog(activity, R.string.task_list,
                NamedUtil.getNames(taskLists.getValue()), pair.first, getDialogResultConsumer(),
                refreshable, scrollable).show();
    }

}
