package com.example.miner34006.comexampleminer34006weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherData implements Parcelable {
    private String mClouds = "None";
    private String mWind = "None";
    private String mPressure = "None";
    private String mHumidity = "None";
    private int mWeatherImageResource = 0;

    public int getmWeatherImageResource() {
        return mWeatherImageResource;
    }

    public void setmWeatherImageResource(int mWeatherImageResource) {
        this.mWeatherImageResource = mWeatherImageResource;
    }

    public String getmClouds() {
        return mClouds;
    }

    public void setmClouds(String mClouds) {
        this.mClouds = mClouds;
    }

    public String getmWind() {
        return mWind;
    }

    public void setmWind(String mWind) {
        this.mWind = mWind;
    }

    public String getmPressure() {
        return mPressure;
    }

    public void setmPressure(String mPressure) {
        this.mPressure = mPressure;
    }

    public String getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(String mHumidity) {
        this.mHumidity = mHumidity;
    }

    public WeatherData() {
    }

    protected WeatherData(Parcel in) {
        mClouds = in.readString();
        mWind = in.readString();
        mPressure = in.readString();
        mHumidity = in.readString();
        mWeatherImageResource = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mClouds);
        dest.writeString(mWind);
        dest.writeString(mPressure);
        dest.writeString(mHumidity);
        dest.writeInt(mWeatherImageResource);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherData> CREATOR = new Parcelable.Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };
}