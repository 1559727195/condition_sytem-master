package com.crazysunj.domain.entity.repository.weather;

import com.google.gson.JsonObject;
import io.reactivex.Flowable;

public interface WeatherRepository {
    Flowable<JsonObject> getWeatherList(String key, String location, String language, String unit);
}
