package com.oleksandr.weatherviewer;

import com.oleksandr.weatherviewer.models.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("forecast?")
    Call<WeatherData> getWeatherData(@Query("q") String city, @Query("units") String units,
                                     @Query("lang") String lang, @Query("APPID") String appid);
}
