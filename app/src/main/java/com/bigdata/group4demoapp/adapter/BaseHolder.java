package com.bigdata.group4demoapp.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

public class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
