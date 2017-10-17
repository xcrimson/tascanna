package name.glonki.tascanna.util;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Glonki on 16.10.2017.
 */

public class ToastUtil {

    public static void showLongToast(Activity activity, @StringRes int string) {
        Toast.makeText(activity, string, Toast.LENGTH_LONG).show();
    }

}
