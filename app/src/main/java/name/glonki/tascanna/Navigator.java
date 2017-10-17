package name.glonki.tascanna;

import android.app.Activity;
import android.content.Intent;

import name.glonki.tascanna.activities.NewTasksActivity;
import name.glonki.teamwork.data.Project;

/**
 * Created by Glonki on 14.10.2017.
 */

public class Navigator {

    public final static int NEW_TASKS_REQUEST_CODE = 1002;

    public static void openNewTasksActivity(Activity activity, Project project) {
        Intent intent = new Intent(activity, NewTasksActivity.class);
        intent.putExtra(NewTasksActivity.PROJECT_PARCELABLE_KEY, project);
        activity.startActivityForResult(intent, NEW_TASKS_REQUEST_CODE);
    }

}
