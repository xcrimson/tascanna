package name.glonki.tascanna.statemachine;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

/**
 * Created by Glonki on 15.10.2017.
 */

public class FSMUtil {

    public static <A,B> Action fireTrigger(final StateMachine<A,B> stateMachine, final B trigger) {
        return new Action() {
            @Override
            public void doIt() {
                stateMachine.fire(trigger);
            }
        };
    }

    public static Action finishActivity(final Activity activity) {
        return new Action() {
            @Override
            public void doIt() {
                activity.finish();
            }
        };
    }

    public static Action finishActivityWithResult(final Activity activity, final int result) {
        return new Action() {
            @Override
            public void doIt() {
                activity.setResult(result);
                activity.finish();
            }
        };
    }

    public static Action setViewVisible(final Activity activity, final @IdRes int id, final boolean visible) {
        return new Action() {
            @Override
            public void doIt() {
                setViewVisible(activity.findViewById(id), visible);
            }
        };
    }

    public static Action setViewsVisible(final Activity activity, final @IdRes int[] ids, final boolean visible) {
        return new Action() {
            @Override
            public void doIt() {
                for(int id : ids) {
                    setViewVisible(activity.findViewById(id), visible);
                }
            }
        };
    }

    private static void setViewVisible(View view, boolean visible) {
        view.setVisibility(visible? View.VISIBLE : View.GONE);
    }

}
