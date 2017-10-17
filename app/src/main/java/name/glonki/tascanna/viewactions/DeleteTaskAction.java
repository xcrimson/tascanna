package name.glonki.tascanna.viewactions;

import android.util.Pair;

import name.glonki.list.ViewAction;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.Deleter;
import name.glonki.tascanna.data.NewTask;

/**
 * Created by Glonki on 17.10.2017.
 */

public class DeleteTaskAction implements ViewAction<NewTask, Object> {

    private final Deleter<NewTask> deleter;

    public DeleteTaskAction(Deleter<NewTask> deleter) {
        this.deleter = deleter;
    }

    @Override
    public int getViewId() {
        return R.id.delete_task;
    }

    @Override
    public void accept(Pair<NewTask, Object> pair) throws Exception {
        deleter.delete(pair.first);
    }

}
