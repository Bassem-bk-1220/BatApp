package com.hb.batapp.fragment

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hb.batapp.R
import com.hb.batapp.adapters.WeatherDailyAdapter
import com.hb.batapp.adapters.WeatherHourlyAdapter
import com.hb.batapp.models.GeneralWeatherResponse
import com.hb.batapp.utils.*
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    private var locationLiveData = MutableLiveData<Location?>()

    private val viewModel: HomeViewModel =
        ViewModelProvider(this).get(HomeViewModel::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
        initObserver()
    }

    private fun initObserver() {
        locationLiveData.observe(requireActivity(), androidx.lifecycle.Observer { location ->
            GlobalScope.launch {
                location?.let {
                    viewModel.getWeatherData(it, getString(R.string.WEATHER_API))
                }
            }
        })

        viewModel.weatherData.observe(requireActivity(), androidx.lifecycle.Observer { response ->
            response?.let {
                setWeatherData(response)
            } ?: kotlin.run {
                Toast.makeText(
                    activity,
                    getString(R.string.no_weather_info),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setWeatherData(response: GeneralWeatherResponse) {
        Log.e("HOME", response.toString())
        tv_temp.text = response.current?.temp?.convertToCelcius()
        tv_name_location.text = response.timeZone
        val weather = response.current?.let { it.weather[0] }
        tv_weather.text = "${weather?.main} - ${weather?.description}"
        when (weather?.main) {
            WEATHER_CLEAR -> {
                setWeatherBackground(R.drawable.sunny_bg)
            }
            WEATHER_RAIN -> {
                setWeatherBackground(R.drawable.rain_bg)
            }
            WEATHER_CLOUDS -> {
                setWeatherBackground(R.drawable.cloudy_bg)
            }
        }
        recycler_weather_hourly.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        response.hourly?.let {
            recycler_weather_hourly.adapter = WeatherHourlyAdapter(it)
        }

        recycler_weather_daily.layoutManager = LinearLayoutManager(requireActivity())
        response.daily?.let {
            recycler_weather_daily.adapter = WeatherDailyAdapter(it)
        }

    }

    private fun setWeatherBackground(drawable: Int) {
        constraint_weather.setBackgroundResource(drawable)
    }

    private fun getLocationPermission() {
        val permissionManager = PermissionManager.getInstance(context)
        permissionManager.checkPermissions(
            Collections.singleton(Manifest.permission.ACCESS_FINE_LOCATION),
            object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
                    if (isGpsEnabled(requireActivity())) {
                        GlobalScope.launch(Dispatchers.IO) {
                            locationLiveData.postValue(getDeviceLocation(requireActivity()))
                        }
                    } else {
                        openDialog(
                            requireActivity(),
                            getString(R.string.open_gps),
                            getString(R.string.app_require_permission),
                            getString(R.string.ok),
                            getString(R.string.cancel),
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        )
                    }
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions) {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                    for (deniedPermission in deniedPermissions) {
                        if (deniedPermission.shouldShowRationale()) {
                            // Display a rationale about why this permission is required
                            openDialog(
                                requireActivity(),
                                getString(R.string.open_gps),
                                getString(R.string.app_require_permission),
                                getString(R.string.ok),
                                getString(R.string.cancel),
                                getLocationPermission()
                            )
                        }
                    }
                }
            })
    }

}