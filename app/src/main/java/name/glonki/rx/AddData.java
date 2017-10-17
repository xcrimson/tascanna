package name.glonki.rx;

import android.util.Pair;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by Glonki on 15.10.2017.
 */

public class AddData<A, B> implements Function<B, Pair<A,B>> {

    public static final Predicate<AddData> DATA_NOT_NULL = new Predicate<AddData>() {
        @Override
        public boolean test(@NonNull AddData addData) throws Exception {
            return addData.data != null;
        }
    };

    private A data;

    @Override
    public Pair<A, B> apply(@NonNull B b) throws Exception {
        return new Pair<>(data, b);
    }

    public void setData(A data) {
        this.data = data;
    }

}
