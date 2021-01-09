package com.hb.batapp.fragment

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hb.batapp.models.GeneralWeatherResponse
import com.hb.batapp.network.ApiClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<GeneralWeatherResponse>()
    val weatherData: LiveData<GeneralWeatherResponse> = _weatherData

    fun getWeatherData(location: Location, appId: String) {
        viewModelScope.launch {
            _weatherData.postValue(
                ApiClient.getApiService().getWeatherData(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    appId
                )
            )
        }
    }
}