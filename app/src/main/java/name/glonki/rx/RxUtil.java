package name.glonki.rx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by Glonki on 17/10/2017.
 */

public class RxUtil {

    public static <A> void processCollectionSequenciallyAsync(Scheduler scheduler, List<A> data,
                                                              final Consumer<A> job,
                                                              final Consumer<Map<A, Throwable>> resultConsumer) {

        BiFunction<Map<A, Throwable>, A, Map<A, Throwable>> function = new BiFunction<Map<A, Throwable>, A, Map<A, Throwable>>() {

            @Override
            public Map<A, Throwable> apply(@NonNull Map<A, Throwable> failed, @NonNull A value) throws Exception {
                try {
                    job.accept(value);
                } catch (Throwable e) {
                    failed.put(value, e);
                }
                return failed;
            }
        };

        Observable.fromIterable(data)
                .observeOn(scheduler)
                .reduce(new HashMap<A, Throwable>(), function)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultConsumer);

    }

}
