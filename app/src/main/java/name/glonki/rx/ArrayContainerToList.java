package name.glonki.rx;

import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Glonki on 14.10.2017.
 */

public class ArrayContainerToList<A> implements Function<ArrayContainer<A>, List<A>> {
    @Override
    public List<A> apply(@NonNull ArrayContainer<A> arrayContainer) throws Exception {
        return Arrays.asList(arrayContainer.getArray());
    }
}
