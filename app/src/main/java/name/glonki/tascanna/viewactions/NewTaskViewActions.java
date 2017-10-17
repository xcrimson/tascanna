package name.glonki.tascanna.viewactions;

import android.app.Activity;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import name.glonki.list.ViewAction;
import name.glonki.list.ViewActions;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.Deleter;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.teamwork.data.Person;
import name.glonki.teamwork.data.TaskList;

/**
 * Created by Glonki on 15.10.2017.
 */

public class NewTaskViewActions implements ViewActions<NewTask> {

    private static final ViewAction<NewTask, CharSequence> UPDATE_CONTENT = new ViewAction<NewTask, CharSequence>() {
        @Override
        public int getViewId() {
            return R.id.content;
        }

        @Override
        public void accept(Pair<NewTask, CharSequence> pair) throws Exception {
            pair.first.getTask().setContent(pair.second.toString());
        }
    };

    private static final ViewAction<NewTask, CharSequence> UPDATE_DESCRIPTION = new ViewAction<NewTask, CharSequence>() {
        @Override
        public int getViewId() {
            return R.id.description;
        }

        @Override
        public void accept(Pair<NewTask, CharSequence> pair) throws Exception {
            pair.first.getTask().setDescription(pair.second.toString());
        }
    };

    private final static List<ViewAction<NewTask, CharSequence>> EDIT_TEXT_ACTIONS;
    static {
        EDIT_TEXT_ACTIONS = new ArrayList<>();
        EDIT_TEXT_ACTIONS.add(UPDATE_CONTENT);
        EDIT_TEXT_ACTIONS.add(UPDATE_DESCRIPTION);
    }

    private List<ViewAction<NewTask, Object>> viewClickActions = new ArrayList<>();

    public NewTaskViewActions(List<ViewAction<NewTask, Object>> viewClickActions) {
        this.viewClickActions = viewClickActions;
    }

    public static NewTaskViewActions create(Activity activity, Refreshable refreshable,
                                            Scrollable<NewTask> scrollable,
                                            Deleter<NewTask> deleter,
                                            BehaviorSubject<List<Person>> people,
                                            BehaviorSubject<List<TaskList>> taskLists) {
        String[] priorities = activity.getResources().getStringArray(R.array.priorities);
        List<ViewAction<NewTask, Object>> viewClickActions = new ArrayList<>();
        viewClickActions.add(new DeleteTaskAction(deleter));
        viewClickActions.add(new PeoplePickAction(activity, refreshable, scrollable, people));
        viewClickActions.add(new PriorityPickAction(activity, refreshable, scrollable, priorities));
        viewClickActions.add(new TaskListPickAction(activity, refreshable, scrollable, taskLists));
        viewClickActions.add(new StartDatePickAction(activity, refreshable, scrollable));
        viewClickActions.add(new DueDatePickAction(activity, refreshable, scrollable));
        return new NewTaskViewActions(viewClickActions);
    }

    @Override
    public List<ViewAction<NewTask, CharSequence>> getEditTextActions() {
        return EDIT_TEXT_ACTIONS;
    }

    @Override
    public List<ViewAction<NewTask, Object>> getViewClickActions() {
        return viewClickActions;
    }
}
