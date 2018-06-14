package com.example.miner34006.comexampleminer34006weatherapp.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {

    private final String APP_ID = "2cd9d5977f93afd43ac8f11df0872f3f";
    private final String UNITS = "metric";

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private Interceptor apiSettingsInterceptor = new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("APPID", APP_ID)
                    .addQueryParameter("units", UNITS)
                    .build();

            Request.Builder requestBuilder = original.newBuilder().url(url);

            return chain.proceed(requestBuilder.build());
        }
    };

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(apiSettingsInterceptor);

    private Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = retrofitBuilder.build();

    public WeatherService weatherService = retrofit.create(WeatherService.class);
}


