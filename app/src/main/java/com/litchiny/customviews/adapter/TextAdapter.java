package com.litchiny.customviews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.litchiny.customviews.R;

/**
 * 基本Text展示的adapter
 *
 * @author Litchiny
 */
public class TextAdapter extends BaseAdapter implements View.OnClickListener {

    public OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, Object item, int position) {
        if (item instanceof String && holder instanceof TextViewHolder) {
            TextViewHolder viewHolder = (TextViewHolder) holder;
            viewHolder.tv_item_text.setTag(position);
            viewHolder.tv_item_text.setText((String)item);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextViewHolder holder = new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent,false));
        holder.tv_item_text.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_item_text && null != itemClickListener){
            itemClickListener.setOnItemClickListener((Integer) v.getTag());
        }
    }


    public interface OnItemClickListener {
        void setOnItemClickListener(int position);
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_text;

        public TextViewHolder(View itemView) {
            super(itemView);
            tv_item_text = itemView.findViewById(R.id.tv_item_text);
        }
    }
}
