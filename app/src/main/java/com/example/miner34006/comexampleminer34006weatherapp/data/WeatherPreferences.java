package com.example.miner34006.comexampleminer34006weatherapp.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Pair;

import com.example.miner34006.comexampleminer34006weatherapp.R;


public class WeatherPreferences {
    public static Pair<String, String> getPreferredCityData(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForCityData = context.getString(R.string.pref_city);
        String defaultData = String.valueOf(context.getResources().getInteger(R.integer.saintPetersburg));
        String cityData = prefs.getString(keyForCityData, defaultData);;

        String[] parts = cityData.split(":");
        final String cityId = parts[0];
        final String timeZone = parts[1];

        return new Pair<>(cityId, timeZone);
    }

    public static String getPreferredTimeZone(Context context) {
        return getPreferredCityData(context).second;
    }

    public static String getPreferredCityId(Context context) {
        return getPreferredCityData(context).first;
    }
}
