package com.droibit.boltssamplekotlin;

import com.droibit.boltssamplekotlin.model.Weather;
import com.droibit.boltssamplekotlin.model.WeatherService;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @auther kumagai
 * @since 15/06/15
 */
public class TaskInJava {

    public void onContinueWithTask() {
        final List<String> weathers = new ArrayList<>();
        getWeatherAsync(400040).continueWithTask(new Continuation<String, Task<String>>() {
            @Override
            public Task<String> then(Task<String> task) throws Exception {
                weathers.add(task.getResult());
                return getWeatherAsync(130010);
            }
        }).continueWith(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) throws Exception {
                weathers.add(task.getResult());
                showResult(weathers);
                return null;
            }
        });
    }

    public Task<String> getWeatherAsyncWithCallback(int city) {
        final Task<String>.TaskCompletionSource task = Task.create();
        final WeatherService weatherService = new RestAdapter.Builder()
                                                    .setEndpoint("http://weather.livedoor.com")
                                                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                                                    .build()
                                                    .create(WeatherService.class);
        weatherService.weatherWithCallback(city, new Callback<Weather>() {
            @Override
            public void success(Weather weather, Response response) {
                task.setResult(weather.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                task.setError(error);
            }
        });


        return task.getTask();
    }

    private Task<String> getWeatherAsync(int city) {
        return null;
    }

    private void showResult(List<String> result) {

    }
}
