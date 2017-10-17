package name.glonki.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Glonki on 16.10.2017.
 */

public abstract class ObserverWithData<A, B> implements Observer<A> {

    private B data;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(A aVoid) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public void setData(B data) {
        this.data = data;
    }

    public B getData() {
        return data;
    }

}
