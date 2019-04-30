package com.crazysunj.data.repository.weather;

import com.crazysunj.data.api.HttpHelper;
import com.crazysunj.data.service.WeatherService;
import com.crazysunj.data.util.RxTransformerUtil;
import com.crazysunj.domain.entity.repository.weather.WeatherRepository;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Flowable;
import retrofit2.Response;

public class WeatherDataRepository implements WeatherRepository {
    private WeatherService mWeatherService;

    @Inject
    public WeatherDataRepository(HttpHelper httpHelper) {
        mWeatherService = httpHelper.getWeatherService();
    }

    @Override
    public Flowable<JsonObject> getWeatherList(String key, String location, String language, String unit) {
        return mWeatherService.getWeatherList(key, location, language, unit)
                .map(Response::body)
                .compose(RxTransformerUtil.normalTransformer());
    }
}
