package name.glonki.list;

import java.util.List;

/**
 * Created by Glonki on 15.10.2017.
 */

public interface ViewActions<A> {

    List<ViewAction<A, CharSequence>> getEditTextActions();

    List<ViewAction<A, Object>> getViewClickActions();

}
