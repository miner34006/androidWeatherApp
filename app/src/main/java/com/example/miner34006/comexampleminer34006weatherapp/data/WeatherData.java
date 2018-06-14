package com.example.miner34006.comexampleminer34006weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherData implements Parcelable {
    private String weatherTypeName = "None";
    private String temperature = "None";
    private String clouds = "None";
    private String wind = "None";
    private String pressure = "None";
    private String humidity = "None";
    private int weatherImageResource = 0;

    public String getWeatherTypeName() {
        return weatherTypeName;
    }

    public void setWeatherTypeName(String weatherTypeName) {
        this.weatherTypeName = weatherTypeName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeatherImageResource() {
        return weatherImageResource;
    }

    public void setWeatherImageResource(int weatherImageResource) {
        this.weatherImageResource = weatherImageResource;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public WeatherData() {
    }

    protected WeatherData(Parcel in) {
        weatherTypeName = in.readString();
        temperature = in.readString();
        clouds = in.readString();
        wind = in.readString();
        pressure = in.readString();
        humidity = in.readString();
        weatherImageResource = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(temperature);
        dest.writeString(weatherTypeName);
        dest.writeString(clouds);
        dest.writeString(wind);
        dest.writeString(pressure);
        dest.writeString(humidity);
        dest.writeInt(weatherImageResource);
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