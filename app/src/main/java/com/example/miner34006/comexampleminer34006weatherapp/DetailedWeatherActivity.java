package com.example.miner34006.comexampleminer34006weatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherPreferences;
import com.example.miner34006.comexampleminer34006weatherapp.utils.pojo.forecastWeatherData.ForecastWeatherData;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.miner34006.comexampleminer34006weatherapp.MainActivity.mWeatherApi;

public class DetailedWeatherActivity extends Activity {

    private WeatherAdapter mForecastAdapter;

    private ImageView mBackgroundImage;

    private void initBackgroundImage() {
        mBackgroundImage = findViewById(R.id.backgroundImage);

        int defaultBackground = R.mipmap.background_spb0;
        int drawableId = getIntent().getIntExtra(MainActivity.BACKGROUND_IMAGE_RESOURCE, defaultBackground);
        mBackgroundImage.setImageResource(drawableId);

        mBackgroundImage.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(DetailedWeatherActivity.this)
                        .radius(5)
                        .sampling(4)
                        .async()
                        .capture(mBackgroundImage)
                        .into((ImageView)mBackgroundImage);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    private void initForecastRecycler() {
        RecyclerView mForecastRecyclerView = findViewById(R.id.weatherDataRecyclerView);
        mForecastRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mForecastLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mForecastRecyclerView.setLayoutManager(mForecastLayoutManager);
        mForecastAdapter = new WeatherAdapter(null, this);
        mForecastRecyclerView.setAdapter(mForecastAdapter);
    }

    private void initDetailRecycler() {
        RecyclerView mDetailRecyclerView = findViewById(R.id.detailRecycler);
        mDetailRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mDetailLayoutManager = new LinearLayoutManager(this);
        mDetailRecyclerView.setLayoutManager(mDetailLayoutManager);

        ArrayList<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("HUM.", getIntent().getStringExtra(MainActivity.HUMIDITY_DATA)));
        data.add(new Pair<>("PR.",  getIntent().getStringExtra(MainActivity.PRESSURE_DATA)));
        data.add(new Pair<>("WIND",  getIntent().getStringExtra(MainActivity.WIND_DATA)));
        data.add(new Pair<>("CLOUDS",  getIntent().getStringExtra(MainActivity.CLOUDS_DATA)));

        DetailWeatherAdapter mDetailAdapter = new DetailWeatherAdapter(data);
        mDetailRecyclerView.setAdapter(mDetailAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);
        initBackgroundImage();

        initForecastRecycler();
        initDetailRecycler();

        int detailWeatherImage = Integer.parseInt(getIntent().getStringExtra(MainActivity.WEATHER_IMAGE));
        ((ImageView) findViewById(R.id.detailedWeatherImageView)).setImageResource(detailWeatherImage);

        //((TextClock) findViewById(R.id.timeTextView)).setTimeZone(MainActivity.TIME_ZONE);

        ((ImageButton) findViewById(R.id.hideImageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Call<ForecastWeatherData> call = mWeatherApi.getForecastWeather(WeatherPreferences.getPreferredCityId(this));
        call.enqueue(new Callback<ForecastWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<ForecastWeatherData> call, @NonNull Response<ForecastWeatherData> response) {
                ForecastWeatherData data = response.body();
                if (data != null) {
                    mForecastAdapter.setDataSet(data);
                    ((TextView) findViewById(R.id.cityTextView)).setText(data.getCity().getName());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastWeatherData> call, @NonNull Throwable t) {
                Log.e("RESPONSE_ERROR", t.getMessage());
            }
        });
    }
}
