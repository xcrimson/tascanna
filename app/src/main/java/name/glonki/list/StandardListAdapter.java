package name.glonki.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import name.glonki.tascanna.data.Refreshable;

/**
 * Created by Glonki on 14.10.2017.
 */

public class StandardListAdapter<A> extends RecyclerView.Adapter<ClickableViewHolder> {

    public static final int TYPE_ITEM = 0;

    protected List<A> dataset = new ArrayList<>();

    private final ViewSetter<A> setter;

    private ViewActions<A> viewActions;

    public StandardListAdapter(@NonNull ViewSetter<A> setter, ViewActions<A> viewActions) {
        this.setter = setter;
        this.viewActions = viewActions;
    }

    public void setData(@NonNull List<A> data) {
        dataset.clear();
        dataset.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<A> data) {
        dataset.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void addItem(A item) {
        dataset.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(A item) {
        dataset.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public ClickableViewHolder<A> onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        int layoutId = setter.getLayoutId();
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ClickableViewHolder<A> holder = ClickableViewHolder.create(v, viewActions);
        return holder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        holder.setData(dataset.get(position));
        setter.setView(holder.getView(), getItemSafe(position), getItemSafe(position-1), getItemSafe(position+1));
    }

    private A getItemSafe(int position) {
        return dataset == null || position < 0 || position >= dataset.size()? null : dataset.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public List<A> getData() {
        return dataset;
    }

    public int getItemPosition(A item) {
        int position = -1;
        for(int i=0; i<dataset.size(); i++) {
            if(dataset.get(i).equals(item)) {
                position = i;
                break;
            }
        }
        return position;
    }

}
