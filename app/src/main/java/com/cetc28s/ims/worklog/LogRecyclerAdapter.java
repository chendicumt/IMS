package com.cetc28s.ims.worklog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.utils.OnItemClickListener;
import com.cetc28s.ims.worklog.beans.LogInfo;
import com.cetc28s.ims.worklog.beans.LogRecyInfoBean;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/18.
 * Version 1.0
 */

public class LogRecyclerAdapter extends RecyclerView.Adapter<LogRecyclerAdapter.MyHolder>{

    private List<LogRecyInfoBean> list;
    private OnItemClickListener mOnItemClickListener;

    public LogRecyclerAdapter(List<LogRecyInfoBean> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
       this. mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_third_recycler,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.textView1.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getTime());
        holder.textView3.setText(list.get(position).getWorkContent());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        public MyHolder(View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.fragment_third_recycler_name);
            textView2=(TextView)itemView.findViewById(R.id.fragment_third_recycler_time);
            textView3=(TextView)itemView.findViewById(R.id.fragment_third_recycler_todayWork);
        }
    }
}
