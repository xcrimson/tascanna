package name.glonki.tascanna.viewactions;

import android.app.Activity;
import android.util.Pair;

import io.reactivex.functions.Consumer;
import name.glonki.list.ViewDialogAction;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.tascanna.util.DialogUtil;

/**
 * Created by Glonki on 17.10.2017.
 */

public class PriorityPickAction implements ViewDialogAction<NewTask,Object,Integer> {

    private final Activity activity;
    private final Refreshable refreshable;
    private final Scrollable<NewTask> scrollable;
    private final String[] priorities;

    public PriorityPickAction(Activity activity, Refreshable refreshable,
                              Scrollable<NewTask> scrollable, String[] priorities) {
        this.activity = activity;
        this.refreshable = refreshable;
        this.scrollable = scrollable;
        this.priorities = priorities;
    }

    @Override
    public Consumer<Pair<NewTask, Integer>> getDialogResultConsumer() {
        return new Consumer<Pair<NewTask, Integer>>() {
            @Override
            public void accept(Pair<NewTask, Integer> pair) {
                pair.first.getTask().setPriority(priorities[pair.second]);
            }
        };
    }

    @Override
    public int getViewId() {
        return R.id.priority;
    }

    @Override
    public void accept(Pair<NewTask, Object> pair) throws Exception {
        DialogUtil.getListSelectorDialog(activity, R.string.priority,
                priorities, pair.first, getDialogResultConsumer(),
                refreshable, scrollable).show();
    }

}