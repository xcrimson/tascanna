package name.glonki.tascanna.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import name.glonki.tascanna.data.Refreshable;
import name.glonki.tascanna.data.Scrollable;
import name.glonki.teamwork.util.DateUtil;

/**
 * Created by Glonki on 15.10.2017.
 */

public class DialogUtil {

    public static <A> DatePickerDialog getDatePickerDialog(Activity activity, @StringRes int title,
                                                           int year, int month, int day, final A data,
                                                           final Consumer<Pair<A, String>> consumer,
                                                           final Refreshable refreshable,
                                                           final Scrollable<A> scrollable) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                try {
                    consumer.accept(new Pair<>(data, DateUtil.getDateString(year, month + 1, day)));
                    refreshable.refresh();
                    scrollable.scrollTo(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(activity, listener, year, month, day);
        dialog.setTitle(title);
        return dialog;
    }

    public static <A> AlertDialog getListSelectorDialog(Activity activity, @StringRes int title,
                                                        String[] items, final A data,
                                                        final Consumer<Pair<A, Integer>> consumer,
                                                        final Refreshable refreshable,
                                                        final Scrollable<A> scrollable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            consumer.accept(new Pair(data, which));
                            refreshable.refresh();
                            scrollable.scrollTo(data);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public static <A> AlertDialog getMultipleItemsDialog(Activity activity, @StringRes int title,
                                                         String[] items, final A data,
                                                         final Consumer<Pair<A, List<Integer>>> consumer,
                                                         final Refreshable refreshable,
                                                         final Scrollable<A> scrollable) {
        final List<Integer> mSelectedItems = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the dialog title
        builder.setTitle(title)
                .setMultiChoiceItems(items, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            consumer.accept(new Pair(data, mSelectedItems));
                            refreshable.refresh();
                            scrollable.scrollTo(data);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                    }
                });

        return builder.create();
    }

}
