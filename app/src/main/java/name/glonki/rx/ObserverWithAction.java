package name.glonki.rx;

import com.github.oxo42.stateless4j.delegates.Action;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Glonki on 15.10.2017.
 */

public class ObserverWithAction implements Observer<Object> {

    private Action action;

    public ObserverWithAction(Action action) {
        this.action = action;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Object a) {
        action.doIt();
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

}
