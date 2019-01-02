package com.litchiny.customviews.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 父adapter
 *
 * @author Litchiny
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.Adapter> extends RecyclerView.Adapter {

    private List<T> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void bindData(List<T> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final T item = items.get(position);
        this.onBindViewHolder(holder,item,position);
    }

    public abstract void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,T item, int position);
}
