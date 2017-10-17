package name.glonki.teamwork.util;

import android.util.Pair;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by Glonki on 15.10.2017.
 */

public class RxUtil {

    public final static Predicate<Pair<?,?>> PAIR_FIRST_NOT_NULL = new Predicate<Pair<?, ?>>() {
        @Override
        public boolean test(@NonNull Pair<?, ?> pair) throws Exception {
            return pair.first != null;
        }
    };

}
