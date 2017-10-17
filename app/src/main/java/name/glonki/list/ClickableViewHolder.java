package name.glonki.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import name.glonki.rx.AddData;
import name.glonki.teamwork.util.RxUtil;

/**
 * Created by Glonki on 14.10.2017.
 */

public class ClickableViewHolder<A> extends RecyclerView.ViewHolder {

    private View view;
    private AddData<A, CharSequence> addDataToString;
    private AddData<A, Object> addDataToObject;

    public ClickableViewHolder(View view, AddData<A, CharSequence> addDataToString,
                               AddData<A, Object> addDataToObject) {
        super(view);
        this.view = itemView;
        this.addDataToString = addDataToString;
        this.addDataToObject = addDataToObject;
    }

    public static <A> ClickableViewHolder<A> create(View view, ViewActions<A> viewActions) {
        AddData<A, CharSequence> addDataToString = new AddData<>();
        AddData<A, Object> addDataToObject = new AddData<>();
        if(viewActions.getEditTextActions() != null) {
            for (ViewAction<A, CharSequence> action : viewActions.getEditTextActions()) {
                EditText editText = view.findViewById(action.getViewId());
                RxTextView.textChanges(editText).map(addDataToString)
                        .filter(RxUtil.PAIR_FIRST_NOT_NULL).subscribe(action);
            }
        }
        if(viewActions.getViewClickActions() != null) {
            for (ViewAction<A, Object> action : viewActions.getViewClickActions()) {
                RxView.clicks(view.findViewById(action.getViewId())).map(addDataToObject)
                        .filter(RxUtil.PAIR_FIRST_NOT_NULL).subscribe(action);
            }
        }
        return new ClickableViewHolder(view, addDataToString, addDataToObject);
    }

    public void setData(A data) {
        addDataToString.setData(data);
        addDataToObject.setData(data);
    }

    public View getView() {
        return view;
    }

}
