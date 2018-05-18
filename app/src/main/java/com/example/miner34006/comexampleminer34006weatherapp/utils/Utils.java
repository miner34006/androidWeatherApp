package com.example.miner34006.comexampleminer34006weatherapp.utils;

import com.example.miner34006.comexampleminer34006weatherapp.R;

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


    public static int getImageSource(Integer weatherId, long unixTime, String timeZone) {
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
}
