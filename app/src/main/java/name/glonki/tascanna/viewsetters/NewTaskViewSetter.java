package name.glonki.tascanna.viewsetters;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import io.reactivex.subjects.BehaviorSubject;
import name.glonki.list.ViewSetter;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.NewTask;
import name.glonki.teamwork.data.Person;
import name.glonki.teamwork.data.Task;
import name.glonki.teamwork.data.TaskList;
import name.glonki.teamwork.util.NamedUtil;
import name.glonki.teamwork.util.NumberUtil;

/**
 * Created by Glonki on 14.10.2017.
 */

public class NewTaskViewSetter implements ViewSetter<NewTask> {

    private BehaviorSubject<List<TaskList>> taskLists;
    private BehaviorSubject<Map<Integer, Person>> peopleMap;

    public NewTaskViewSetter(BehaviorSubject<List<TaskList>> taskLists,
                             BehaviorSubject<Map<Integer, Person>> peopleMap) {
        this.taskLists = taskLists;
        this.peopleMap = peopleMap;
    }

    @Override
    public void setView(View view, NewTask current, Object previous, Object next) {

        EditText content = view.findViewById(R.id.content);
        EditText description = view.findViewById(R.id.description);
        TextView taskList = view.findViewById(R.id.task_list);
        TextView startDate = view.findViewById(R.id.start_date);
        TextView dueDate = view.findViewById(R.id.due_date);
        TextView priority = view.findViewById(R.id.priority);
        TextView responsible = view.findViewById(R.id.responsible_party);
        Button deleteTask = view.findViewById(R.id.delete_task);
        View divider = view.findViewById(R.id.divider);

        Task task = current.getTask();
        content.setText(task.getContent());
        description.setText(task.getDescription());
        setText(taskList, R.string.label_task_list, current.getTaskList().getName());
        setText(startDate, R.string.label_start_date, formatDate(task.getStartDate()));
        setText(dueDate, R.string.label_due_date, formatDate(task.getDueDate()));
        setText(priority, R.string.label_priority, task.getPriority());
        String responsibleParty = NamedUtil.getNamesByIds(peopleMap.getValue(),
                NumberUtil.parseNumbers(current.getTask().getResponsibleParty()));
        setText(responsible, R.string.label_responsible, responsibleParty);

        deleteTask.setVisibility(previous != null || next != null? View.VISIBLE : View.GONE);
        divider.setVisibility(next == null? View.GONE : View.VISIBLE);
        taskList.setVisibility(taskLists.getValue().size() > 1? View.VISIBLE : View.GONE);

    }

    private void setText(TextView textView, @StringRes int prefix, String text) {
        textView.setText(prefix);
        textView.append(" ");
        textView.append(text);
    }

    private static String formatDate(String date) {
        String result;
        if(date == null || date.length() < 8) {
            result = date;
        } else {
            result = date.substring(0, 4)
                    .concat("/")
                    .concat(date.substring(4, 6))
                    .concat("/")
                    .concat(date.substring(6));
        }
        return result;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_task;
    }

}
