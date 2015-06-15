package com.droibit.boltssamplekotlin.model;

import android.support.annotation.NonNull;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kumagai on 2015/02/09.
 */
public interface WeatherService {

    @GET("/forecast/webservice/json/v1")
    Weather weather(@Query("city") @NonNull Integer city);

    @GET("/forecast/webservice/json/v1")
    void weatherWithCallback(@Query("city") @NonNull Integer city, Callback<Weather> callback);
}