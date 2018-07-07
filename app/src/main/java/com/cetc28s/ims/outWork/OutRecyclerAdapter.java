package com.cetc28s.ims.outWork;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.outWork.beans.OutRecyInfo;
import com.cetc28s.ims.utils.OnItemClickListener;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/2.
 * Version 1.0
 */

public class OutRecyclerAdapter extends RecyclerView.Adapter<OutRecyclerAdapter.MyViewHoler> {

    private OnItemClickListener mOnItemClickListener;
    private List<OutRecyInfo> list;

    public OutRecyclerAdapter(List<OutRecyInfo> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_first_recycler,parent,false);
        MyViewHoler viewHoler = new MyViewHoler(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(final MyViewHoler holder, final int position) {
        holder.textView1.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getAddress());
        holder.textView3.setText(list.get(position).getStartTime());
        holder.textView4.setText(list.get(position).getPlanEndTime());
        holder.textView5.setText(list.get(position).getRealEndTime());

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHoler extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        public MyViewHoler(View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.fragment_first_recycler_name);
            textView2=(TextView)itemView.findViewById(R.id.fragment_first_recycler_address);
            textView3=(TextView)itemView.findViewById(R.id.fragment_first_recycler_startTime);
            textView4=(TextView)itemView.findViewById(R.id.fragment_first_recycler_plan_endTime);
            textView5=(TextView)itemView.findViewById(R.id.fragment_first_recycler_real_endTime);
        }
    }
}
