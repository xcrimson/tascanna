package name.glonki.teamwork.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Glonki on 17/10/2017.
 */

public class TaskToAdd {

    @SerializedName("todo-item")
    private final Task taskToAdd;

    public TaskToAdd(Task taskToAdd) {
        this.taskToAdd = taskToAdd;
    }

}
