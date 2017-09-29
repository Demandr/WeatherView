package com.oleksandr.weatherviewer.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.oleksandr.weatherviewer.models.WeatherData;

public interface WeatherView extends MvpView {
    void updateWeatherView(WeatherData weather);

}
