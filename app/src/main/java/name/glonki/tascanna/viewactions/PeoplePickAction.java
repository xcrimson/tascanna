package name.glonki.tascanna.viewactions;

import android.app.Activity;
import android.util.Pair;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import name.glonki.list.ViewDialogAction;
import name.glonki.tascanna.R;
import name.glonki.tascanna.data.NewTask;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.tascanna.util.DialogUtil;
import name.glonki.tascanna.util.ToastUtil;
import name.glonki.teamwork.data.Person;
import name.glonki.teamwork.util.NamedUtil;

/**
 * Created by Glonki on 17.10.2017.
 */

public class PeoplePickAction implements ViewDialogAction<NewTask, Object, List<Integer>> {

    private final Activity activity;
    private final Refreshable refreshable;
    private final Scrollable<NewTask> scrollable;
    private final BehaviorSubject<List<Person>> people;

    public PeoplePickAction(Activity activity, Refreshable refreshable,
                            Scrollable<NewTask> scrollable, BehaviorSubject<List<Person>> people) {
        this.activity = activity;
        this.refreshable = refreshable;
        this.scrollable = scrollable;
        this.people = people;
    }

    @Override
    public Consumer<Pair<NewTask, List<Integer>>> getDialogResultConsumer() {
        return new Consumer<Pair<NewTask, List<Integer>>>() {
            @Override
            public void accept(Pair<NewTask, List<Integer>> pair) {
                if(pair.second.size() == 0) {
                    ToastUtil.showLongToast(activity, R.string.no_person_selected);
                } else {
                    pair.first.getTask().setResponsibleParty(
                            Person.getPartiesIds(people.getValue(), pair.second));
                }
            }
        };
    }

    @Override
    public int getViewId() {
        return R.id.responsible_party;
    }

    @Override
    public void accept(Pair<NewTask, Object> pair) throws Exception {
        DialogUtil.getMultipleItemsDialog(activity, R.string.responsible_party,
                NamedUtil.getNames(people.getValue()), pair.first,
                getDialogResultConsumer(), refreshable, scrollable).show();
    }

}
