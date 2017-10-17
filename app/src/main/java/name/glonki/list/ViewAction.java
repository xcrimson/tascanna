package name.glonki.list;

import android.support.annotation.IdRes;
import android.util.Pair;

import io.reactivex.functions.Consumer;

/**
 * Created by Glonki on 15.10.2017.
 */

public interface ViewAction<A, B> extends Consumer<Pair<A, B>> {

    @IdRes
    int getViewId();

}
