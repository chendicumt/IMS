package com.cetc28s.ims.myInfo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cetc28s.ims.R;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/5/4.
 * Version 1.0
 */

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.MyHolder> {

    private List<TeamRecyInfo> list;

    public TeamRecyclerAdapter(List<TeamRecyInfo> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.text1.setText(list.get(position).getName());
        holder.text2.setText(list.get(position).getState()+"（"+list.get(position).getMessage()+"）");
        if (list.get(position).getMessage().equals("")){
            holder.text2.setText(list.get(position).getState());
        }
        holder.text3.setText(list.get(position).getStartTime());
        holder.text4.setText(list.get(position).getEndTime());
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_team_recycler,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        public MyHolder(View itemView) {
            super(itemView);
            text1 = (TextView)itemView.findViewById(R.id.team_name);
            text2 = (TextView)itemView.findViewById(R.id.team_state);
            text3 = (TextView)itemView.findViewById(R.id.team_startTime);
            text4 = (TextView)itemView.findViewById(R.id.team_endTime);
        }
    }
}
