package name.glonki.tascanna.data;

import name.glonki.teamwork.data.Task;
import name.glonki.teamwork.data.TaskList;
import name.glonki.teamwork.data.TaskToAdd;

/**
 * Created by Glonki on 15.10.2017.
 */

public class NewTask {

    private TaskList taskList;
    private Task task;

    public NewTask(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    public static NewTask create(TaskList taskList) {
        return new NewTask(taskList, Task.newTaskWeekFromToday());
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public Task getTask() {
        return task;
    }

    public TaskToAdd getTaskToAdd() {
        return new TaskToAdd(task);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        appendString(builder, "task list: ", taskList.getName());
        appendString(builder, "content: ", task.getContent());
        appendString(builder, "description: ", task.getDescription());
        appendString(builder, "responsible: ", task.getResponsibleParty());
        appendString(builder, "start: ", task.getStartDate());
        appendString(builder, "due: ", task.getDueDate());
        appendString(builder, "priority: ", task.getPriority());
        return builder.toString();
    }

    private static void appendString(StringBuilder builder, String prefix, Object value) {
        builder.append(prefix);
        builder.append(value);
        builder.append("\n");
    }

}
