package com.example.miner34006.comexampleminer34006weatherapp.activities;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.miner34006.comexampleminer34006weatherapp.R;
import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherData;
import com.example.miner34006.comexampleminer34006weatherapp.data.WeatherPreferences;
import com.example.miner34006.comexampleminer34006weatherapp.network.NetworkModule;
import com.example.miner34006.comexampleminer34006weatherapp.Utils;
import com.example.miner34006.comexampleminer34006weatherapp.network.WeatherService;
import com.example.miner34006.comexampleminer34006weatherapp.pojo.currentWeatherData.CurrentWeatherResponse;

import java.io.IOException;
import java.util.Random;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderCallbacks<CurrentWeatherResponse> {

    private ImageView mBackgroundImage;
    private TextClock mTextClock;
    private TextView mCityTextView;

    private WeatherData mWeatherData = new WeatherData();

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

                intent.putExtra("BACKGROUND_IMAGE", (int) mBackgroundImage.getTag());
                intent.putExtra("WEATHER_DATA", mWeatherData);

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
        mTextClock.setTimeZone(WeatherPreferences.getPreferredTimeZone(this));
        mCityTextView.setText(WeatherPreferences.getPreferredCityName(this));
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

        mCityTextView =  findViewById(R.id.cityTextView);
        mCityTextView.setText(WeatherPreferences.getPreferredCityName(MainActivity.this));

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
            mWeatherData = Utils.createWeatherDataFromResponse(data, MainActivity.this);
            ((ImageView) findViewById(R.id.weatherTypeImageView)).setImageResource(mWeatherData.getmWeatherImageResource());
            ((TextView) findViewById(R.id.weatherTypeTextView)).setText(mWeatherData.getmWeatherTypeName());
            ((TextView) findViewById(R.id.todayTemperature)).setText(mWeatherData.getmTemperature() + "\u00B0");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<CurrentWeatherResponse> loader) {}
}
