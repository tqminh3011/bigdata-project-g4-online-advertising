package com.bigdata.group4demoapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface BaseAdapterListener {
    void onItemClicked(RecyclerView.ViewHolder holder, int position);
}
