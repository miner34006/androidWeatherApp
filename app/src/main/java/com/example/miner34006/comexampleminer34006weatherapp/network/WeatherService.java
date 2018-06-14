package com.example.miner34006.comexampleminer34006weatherapp.network;

import com.example.miner34006.comexampleminer34006weatherapp.pojo.currentWeatherData.CurrentWeatherResponse;
import com.example.miner34006.comexampleminer34006weatherapp.pojo.forecastWeatherData.ForecastWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeatherData(@Query("id") String cityId);

    @GET("forecast")
    Call<ForecastWeatherData> getForecastWeather(@Query("id") String cityId);

}
