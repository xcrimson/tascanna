package name.glonki.rx;

import android.util.Pair;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by Glonki on 14.10.2017.
 */

public class ToPair<A, B> implements BiFunction<A, B, Pair<A, B>> {

    @Override
    public Pair<A, B> apply(@NonNull A a, @NonNull B b) throws Exception {
        return new Pair<>(a, b);
    }

}
