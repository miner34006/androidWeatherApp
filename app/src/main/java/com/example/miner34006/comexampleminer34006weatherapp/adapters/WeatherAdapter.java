package com.example.miner34006.comexampleminer34006weatherapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miner34006.comexampleminer34006weatherapp.R;
import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherPreferences;
import com.example.miner34006.comexampleminer34006weatherapp.Utils;
import com.example.miner34006.comexampleminer34006weatherapp.pojo.forecastWeatherData.ForecastWeatherData;
import com.example.miner34006.comexampleminer34006weatherapp.pojo.forecastWeatherData.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private ForecastWeatherData mDataSet;
    private Context mContext;

    public WeatherAdapter(ForecastWeatherData mDataSet, Context mContext) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    public void setDataSet(ForecastWeatherData mDataSet) {
        this.mDataSet = mDataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutTemplate = R.layout.item_weather;
        View view = LayoutInflater.from(context).inflate(layoutTemplate, parent, false);

        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        if (mDataSet != null) {
            List weatherAtTime = mDataSet.getList()[position];
            long unixTime = Long.parseLong(weatherAtTime.getDt());
            int weatherTypeId = Integer.parseInt(weatherAtTime.getWeather()[0].getId());


            String timeZone =  WeatherPreferences.getPreferredTimeZone(mContext);
            String formattedTime = Utils.transformFromUnixTime(unixTime, timeZone);
            int temperature = Math.round(Float.parseFloat(weatherAtTime.getMain().getTemp()));
            int weatherImageResource = Utils.getImageResource(weatherTypeId, unixTime, timeZone);

            holder.mForecastTimeTextView.setText(formattedTime);
            holder.mForecastTemperatureTextView.setText(temperature + "\u00B0");
            holder.mForecastImageView.setImageResource(weatherImageResource);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet != null) {
            return 9;
        } else {
            return 0;
        }
    }

    class WeatherHolder extends RecyclerView.ViewHolder {

        final TextView mForecastTimeTextView;
        final ImageView mForecastImageView;
        final TextView mForecastTemperatureTextView;

        WeatherHolder(View itemView) {
            super(itemView);

            mForecastTimeTextView = itemView.findViewById(R.id.forecastTimeTextView);
            mForecastImageView = itemView.findViewById(R.id.forecastImageView);
            mForecastTemperatureTextView = itemView.findViewById(R.id.forecastTemperatureTextView);
        }
    }
}
