package name.glonki.teamwork.data;

import name.glonki.rx.ArrayContainer;

/**
 * Created by Glonki on 14.10.2017.
 */

public class TaskLists implements ArrayContainer<TaskList> {

    private final TaskList[] tasklists;

    public TaskLists(TaskList[] tasklists) {
        this.tasklists = tasklists;
    }

    public TaskList[] getTaskLists() {
        return tasklists;
    }

    @Override
    public TaskList[] getArray() {
        return tasklists;
    }
}
