package name.glonki.tascanna.viewactions;

import java.util.ArrayList;
import java.util.List;

import name.glonki.list.ViewAction;
import name.glonki.list.ViewActions;
import name.glonki.teamwork.data.Project;

/**
 * Created by Glonki on 15.10.2017.
 */

public class ProjectViewActions implements ViewActions<Project> {

    private final List<ViewAction<Project, Object>> viewClickActions;

    public ProjectViewActions(List<ViewAction<Project, Object>> viewClickActions) {
        this.viewClickActions = viewClickActions;
    }

    public static ProjectViewActions create(ViewAction<Project, Object> newTasksClickAction) {
        List<ViewAction<Project, Object>> viewClickActions = new ArrayList<>();
        viewClickActions.add(newTasksClickAction);
        return new ProjectViewActions(viewClickActions);
    }

    @Override
    public List<ViewAction<Project, CharSequence>> getEditTextActions() {
        return null;
    }

    @Override
    public List<ViewAction<Project, Object>> getViewClickActions() {
        return viewClickActions;
    }
}
