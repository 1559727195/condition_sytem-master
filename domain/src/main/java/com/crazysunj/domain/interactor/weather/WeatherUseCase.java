package com.crazysunj.domain.interactor.weather;

import com.crazysunj.domain.entity.repository.weather.WeatherRepository;

import javax.inject.Inject;

public class WeatherUseCase {
    private final WeatherRepository mWeatherRepository;

    @Inject
    public WeatherUseCase(WeatherRepository weatherRepository) {
        mWeatherRepository = weatherRepository;
    }

}
