package com.cetc28s.ims.leave;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.leave.beans.LeaveRecyInfo;
import com.cetc28s.ims.utils.OnItemClickListener;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/16.
 * Version 1.0
 */

public class LeaveRecyclerAdapter  extends RecyclerView.Adapter<LeaveRecyclerAdapter.MyHolder>{

    private List<LeaveRecyInfo> list;
    private OnItemClickListener mOnItemClickListener;
    public LeaveRecyclerAdapter(List<LeaveRecyInfo> list){
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_second_recycler,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.textView1.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getLeaveType());
        holder.textView3.setText(list.get(position).getStartTime());
        holder.textView4.setText(list.get(position).getEndTime());
        holder.textView5.setText(list.get(position).getDayLength());

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =holder.getAdapterPosition();
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
        TextView textView4;
        TextView textView5;
        public MyHolder(View itemView) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.fragment_second_recycler_name);
            textView2=(TextView)itemView.findViewById(R.id.fragment_second_recycler_leaveType);
            textView3=(TextView)itemView.findViewById(R.id.fragment_second_recycler_startTime);
            textView4=(TextView)itemView.findViewById(R.id.fragment_second_recycler_endTime);
            textView5=(TextView)itemView.findViewById(R.id.fragment_second_recycler_timeLength);
        }
    }
}
