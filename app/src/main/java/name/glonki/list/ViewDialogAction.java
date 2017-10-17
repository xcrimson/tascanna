package name.glonki.list;

import android.util.Pair;

import io.reactivex.functions.Consumer;

/**
 * Created by Glonki on 17.10.2017.
 */

public interface ViewDialogAction<A, B, C> extends ViewAction<A, B> {

    Consumer<Pair<A, C>> getDialogResultConsumer();

}
