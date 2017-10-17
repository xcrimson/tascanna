package name.glonki.list;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Glonki on 14.10.2017.
 */

public interface ViewSetter<A> {

    void setView(View view, A current, Object previous, Object next);

    @LayoutRes
    int getLayoutId();

}
