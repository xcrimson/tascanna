package name.glonki.tascanna.viewactions;

import android.app.Activity;
import android.util.Pair;

import java.util.Calendar;

import io.reactivex.functions.Consumer;
import name.glonki.list.ViewDialogAction;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.tascanna.util.DialogUtil;
import name.glonki.tascanna.util.ToastUtil;
import name.glonki.teamwork.util.DateUtil;

/**
 * Created by Glonki on 17.10.2017.
 */

public class StartDatePickAction implements ViewDialogAction<NewTask, Object, String> {

    private final Activity activity;
    private final Refreshable refreshable;
    private final Scrollable<NewTask> scrollable;

    public StartDatePickAction(Activity activity, Refreshable refreshable,
                               Scrollable<NewTask> scrollable) {
        this.activity = activity;
        this.refreshable = refreshable;
        this.scrollable = scrollable;
    }

    @Override
    public Consumer<Pair<NewTask, String>> getDialogResultConsumer() {
        return new Consumer<Pair<NewTask, String>>() {
            @Override
            public void accept(Pair<NewTask, String> pair) {
                String now = DateUtil.getDateString(Calendar.getInstance());
                if(pair.second.compareTo(now) < 0) {
                    ToastUtil.showLongToast(activity, R.string.start_date_in_past);
                } else {
                    pair.first.getTask().setStartDate(pair.second);
                    if(pair.first.getTask().getDueDate().compareTo(pair.second) < 0) {
                        pair.first.getTask().setDueDate(pair.second);
                    }
                }
            }
        };
    }

    @Override
    public int getViewId() {
        return R.id.start_date;
    }

    @Override
    public void accept(Pair<NewTask, Object> pair) throws Exception {
        int[] ymd = DateUtil.getYearMonthDay(pair.first.getTask().getStartDate());
        DialogUtil.getDatePickerDialog(activity, R.string.start_date, ymd[0], ymd[1], ymd[2],
                pair.first, getDialogResultConsumer(), refreshable, scrollable).show();
    }

}
