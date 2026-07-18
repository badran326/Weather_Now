package com.example.weathernow.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weathernow.model.WeatherData;
import com.example.weathernow.model.WeatherLocation;
import com.example.weathernow.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();
    }

    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadWeather(WeatherLocation location) {
        loading.setValue(true);
        weatherRepository.getWeather(location, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                weatherData.postValue(data);
                loading.postValue(false);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
                loading.postValue(false);
            }
        });
    }

}
