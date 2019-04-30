package com.massky.conditioningsystem.di.module;


import android.content.Context;


import com.crazysunj.data.api.HttpHelper;
import com.crazysunj.data.repository.weather.WeatherDataRepository;
import com.crazysunj.domain.entity.repository.weather.WeatherRepository;
import com.massky.conditioningsystem.Utils.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }


    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(HttpHelper httpHelper) {
        return httpHelper.getOkHttpClient();
    }

    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherDataRepository weatherRepository) {
        return weatherRepository;
    }


}
