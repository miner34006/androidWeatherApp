package com.example.miner34006.comexampleminer34006weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherPreferences;
import com.example.miner34006.comexampleminer34006weatherapp.utils.NetworkModule;
import com.example.miner34006.comexampleminer34006weatherapp.utils.Utils;
import com.example.miner34006.comexampleminer34006weatherapp.utils.WeatherService;
import com.example.miner34006.comexampleminer34006weatherapp.utils.pojo.currentWeatherData.CurrentWeatherResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderCallbacks<CurrentWeatherResponse> {

    private ImageView mBackgroundImage;
    private TextClock mTextClock;

    static final String BACKGROUND_IMAGE_RESOURCE = "com.example.miner34006.backgroundImage";
    static final String WEATHER_IMAGE = "com.example.miner34006.weatherImage";
    static final String HUMIDITY_DATA = "com.example.miner34006.humidity";
    static final String PRESSURE_DATA = "com.example.miner34006.pressure";
    static final String WIND_DATA = "com.example.miner34006.wind";
    static final String CLOUDS_DATA = "com.example.miner34006.clouds";

    private HashMap<String, String> mWeatherData = new HashMap<>();

    public static WeatherService mWeatherApi = new NetworkModule().weatherService;
    private static final int FORECAST_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    private void initBackgroundImage() {
        mBackgroundImage = findViewById(R.id.backgroundImage);
        int[] images = {
                R.mipmap.background_spb0,
                R.mipmap.background_spb1,
                R.mipmap.background_spb2,
                R.mipmap.background_spb3
        };
        int drawableId = images[new Random().nextInt(images.length)];
        mBackgroundImage.setTag(drawableId);
        mBackgroundImage.setImageResource(drawableId);
    }

    private void initExpandButton() {
        ImageButton expandButton = findViewById(R.id.expandImageButton);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class receiverActivity = DetailedWeatherActivity.class;
                Intent intent = new Intent(context, receiverActivity);

                int drawableId = (int) mBackgroundImage.getTag();
                intent.putExtra(BACKGROUND_IMAGE_RESOURCE, drawableId);
                intent.putExtra(HUMIDITY_DATA, mWeatherData.get(HUMIDITY_DATA));
                intent.putExtra(PRESSURE_DATA, mWeatherData.get(PRESSURE_DATA));
                intent.putExtra(WIND_DATA, mWeatherData.get(WIND_DATA));
                intent.putExtra(CLOUDS_DATA, mWeatherData.get(CLOUDS_DATA));
                intent.putExtra(WEATHER_IMAGE, mWeatherData.get(WEATHER_IMAGE));

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void initOptionButton() {
        findViewById(R.id.settingsImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class receiverActivity = SettingsActivity.class;
                Intent intent = new Intent(context, receiverActivity);
                startActivity(intent);
            }
        });
    }

    void updateDataInUi() {
        Pair<String, String> weatherData = WeatherPreferences.getPreferredCityData(this);
        String timeZone = weatherData.second;

        mTextClock.setTimeZone(timeZone);
        getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBackgroundImage();
        initExpandButton();
        initOptionButton();

        mTextClock = findViewById(R.id.timeTextView);
        mTextClock.setTimeZone(WeatherPreferences.getPreferredTimeZone(this));

        LoaderCallbacks<CurrentWeatherResponse> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, callback);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            updateDataInUi();
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @NonNull
    @Override
    public Loader<CurrentWeatherResponse> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<CurrentWeatherResponse>(this) {

            CurrentWeatherResponse cashData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (cashData != null) {
                    deliverResult(cashData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public CurrentWeatherResponse loadInBackground() {
                String cityId = WeatherPreferences.getPreferredCityData(MainActivity.this).first;
                Call<CurrentWeatherResponse> call = mWeatherApi.getCurrentWeatherData(cityId);
                CurrentWeatherResponse response = null;
                try {
                    response = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void deliverResult(@Nullable CurrentWeatherResponse data) {
                cashData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<CurrentWeatherResponse> loader, CurrentWeatherResponse data) {
        if (data != null) {
            long unixTime = System.currentTimeMillis() / 1000L;
            int weatherTypeId = Integer.parseInt(data.getWeather()[0].getId());
            String timeZone = WeatherPreferences.getPreferredTimeZone(MainActivity.this);

            int weatherImageSource = Utils.getImageSource(weatherTypeId, unixTime, timeZone);
            int temperature = Math.round(Float.parseFloat(data.getMain().getTemp()));
            String weatherTypeName = data.getWeather()[0].getMain();
            String temperatureValue = String.valueOf(temperature) + "\u00B0";

            ((ImageView) findViewById(R.id.weatherTypeImageView)).setImageResource(weatherImageSource);
            ((TextView) findViewById(R.id.weatherTypeTextView)).setText(weatherTypeName);
            ((TextView) findViewById(R.id.todayTemperature)).setText(temperatureValue);
            ((TextView) findViewById(R.id.cityTextView)).setText(data.getName());

            mWeatherData.put(WEATHER_IMAGE, String.valueOf(weatherImageSource));
            mWeatherData.put(CLOUDS_DATA, String.valueOf(data.getClouds().getAll() + "%"));
            mWeatherData.put(WIND_DATA, String.valueOf(data.getWind().getSpeed() + "m/c"));
            mWeatherData.put(PRESSURE_DATA, String.valueOf(data.getMain().getPressure() + "hPa"));
            mWeatherData.put(HUMIDITY_DATA, String.valueOf(data.getMain().getHumidity() + "%"));
            mWeatherData.put(BACKGROUND_IMAGE_RESOURCE, String.valueOf(mBackgroundImage.getTag()));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<CurrentWeatherResponse> loader) {}
}
