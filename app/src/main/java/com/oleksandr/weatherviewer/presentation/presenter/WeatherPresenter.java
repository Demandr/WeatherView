package com.oleksandr.weatherviewer.presentation.presenter;


import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleksandr.weatherviewer.R;
import com.oleksandr.weatherviewer.WeatherService;
import com.oleksandr.weatherviewer.models.WeatherData;
import com.oleksandr.weatherviewer.presentation.view.WeatherView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {
    public static final String TAG = "WeatherPresenter";


    private final Context mContext;

    public WeatherPresenter(Context context){
        mContext = context;
    }

    public void loadDataWeather(String city) {
        if (city.isEmpty()){
            Log.e(TAG, "City is empty, incorrect link");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mContext.getString(R.string.web_service_url_new))
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<WeatherData> request = weatherService
                .getWeatherData(city, "metric", mContext.getString(R.string.localization),
                        mContext.getString(R.string.api_key));
        Log.d(TAG, "loadDataWeather: ");
        request.enqueue(weatherCallback);
    }


    private Callback<WeatherData> weatherCallback = new Callback<WeatherData>() {
        @Override
        public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
            if (response.isSuccessful()) {
                Log.d(TAG, "onResponse: " + response.toString());

                WeatherData weathers = response.body();
                getViewState().updateWeatherView(weathers);

                Log.d(TAG, "onResponse: Weather ->  " + weathers.getList().get(0).getMain().getTemp().toString());
            }else {
                Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
            }

        }

        @Override
        public void onFailure(Call<WeatherData> call, Throwable t) {
            t.printStackTrace();
        }
    };

}
