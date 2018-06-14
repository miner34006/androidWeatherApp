package com.example.miner34006.comexampleminer34006weatherapp.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miner34006.comexampleminer34006weatherapp.R;

import java.util.ArrayList;


public class DetailWeatherAdapter extends RecyclerView.Adapter<DetailWeatherAdapter.DetailHolder> {

    private ArrayList<Pair<String, String>> mData;

    public DetailWeatherAdapter(ArrayList<Pair<String, String>> mData) {
        this.mData = mData;
    }

    public void setData(ArrayList<Pair<String, String>> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutTemplate = R.layout.item_detail;
        View view = LayoutInflater.from(context).inflate(layoutTemplate, parent, false);

        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
        holder.mParameter.setText(mData.get(position).first);
        holder.mParameterValue.setText(mData.get(position).second);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class DetailHolder extends RecyclerView.ViewHolder {

        final TextView mParameter;
        final TextView mParameterValue;

        DetailHolder(View itemView) {
            super(itemView);

            mParameter = itemView.findViewById(R.id.parameter);
            mParameterValue = itemView.findViewById(R.id.parameterValue);
        }
    }
}
