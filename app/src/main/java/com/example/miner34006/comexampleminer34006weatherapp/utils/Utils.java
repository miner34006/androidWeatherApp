package com.example.miner34006.comexampleminer34006weatherapp.utils;

import android.content.Context;

import com.example.miner34006.comexampleminer34006weatherapp.R;
import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherData;
import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherPreferences;
import com.example.miner34006.comexampleminer34006weatherapp.utils.pojo.currentWeatherData.CurrentWeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static String transformFromUnixTime(long unixTime, String timeZone) {
        Date date = new java.util.Date(unixTime * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
        return sdf.format(date);
    }


    public static int getImageResource(Integer weatherId, long unixTime, String timeZone) {
        String timeNow = transformFromUnixTime(unixTime, timeZone);
        int weatherImageSource = 0;

        if (weatherId == 800) {
            if (Objects.equals(timeNow, "00") || Objects.equals(timeNow, "03") || Objects.equals(timeNow, "06")){
                weatherImageSource = R.drawable.weather_night;
            } else {
                weatherImageSource = R.drawable.weather_sunny;
            }
        } else {
            int firstDigit = Integer.parseInt(Integer.toString(weatherId).substring(0, 1));
            switch (firstDigit) {
                case 2:
                    weatherImageSource = R.drawable.weather_lightning_rainy;
                    break;
                case 3:
                    weatherImageSource = R.drawable.weather_snowy_rainy;
                    break;
                case 5:
                    weatherImageSource = R.drawable.weather_rainy;
                    break;
                case 6:
                    weatherImageSource = R.drawable.weather_snowy;
                    break;
                case 7:
                    weatherImageSource = R.drawable.weather_fog;
                    break;
                case 8:
                    weatherImageSource = R.drawable.weather_cloudy;
                    break;
            }
        }
        return weatherImageSource;
    }

    public static WeatherData createWeatherDataFromResponse(CurrentWeatherResponse data, Context context) {
        WeatherData weatherData = new WeatherData();

        long unixTime = System.currentTimeMillis();
        int weatherTypeId = Integer.parseInt(data.getWeather()[0].getId());
        String timeZone = WeatherPreferences.getPreferredTimeZone(context);
        int weatherImageResource = Utils.getImageResource(weatherTypeId, unixTime, timeZone);

        weatherData.setWeatherImageResource(weatherImageResource);
        weatherData.setClouds(String.valueOf(data.getClouds().getAll()));
        weatherData.setHumidity(String.valueOf(data.getMain().getHumidity()));
        weatherData.setPressure(String.valueOf(data.getMain().getPressure()));
        weatherData.setWind(String.valueOf(data.getWind().getSpeed()));

        String temperature = String.valueOf(Math.round(Float.parseFloat(data.getMain().getTemp())));
        weatherData.setTemperature(temperature);

        String weatherTypeName = data.getWeather()[0].getMain();
        weatherData.setWeatherTypeName(weatherTypeName);

        return weatherData;
    }
}
