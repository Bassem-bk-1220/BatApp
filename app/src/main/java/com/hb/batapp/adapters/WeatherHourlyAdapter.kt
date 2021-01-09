package com.hb.batapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hb.batapp.R
import com.hb.batapp.models.CurrentWeatherResponse
import com.hb.batapp.utils.*
import kotlinx.android.synthetic.main.item_weather_hourly.view.*

class WeatherHourlyAdapter
    (private val listWeatherHourly: List<CurrentWeatherResponse>) :
    RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_weather_hourly,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvWeatherTime.text = listWeatherHourly[position].dt.convertTimeStampToDate()
        holder.tvWeatherTemperature.text = listWeatherHourly[position].temp.convertToCelcius()

        when (listWeatherHourly[position].weather[0].main) {
            WEATHER_CLEAR -> {
                holder.imgWeather.setImageResource(R.drawable.sun)
            }
            WEATHER_RAIN -> {
                holder.imgWeather.setImageResource(R.drawable.rain)
            }
            WEATHER_CLOUDS -> {
                holder.imgWeather.setImageResource(R.drawable.cloudy)
            }
        }

    }

    override fun getItemCount(): Int {
        return listWeatherHourly.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWeatherTime: TextView = view.tv_weather_time
        val imgWeather: ImageView = view.img_weather
        val tvWeatherTemperature: TextView = view.tv_weather_temp

    }
}