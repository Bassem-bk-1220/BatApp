package com.hb.batapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hb.batapp.R
import com.hb.batapp.models.DailyWeatherResponse
import com.hb.batapp.utils.convertTimeStampToDate
import com.hb.batapp.utils.convertToCelcius
import kotlinx.android.synthetic.main.item_weather_daily.view.*

class WeatherDailyAdapter
    (private val listWeatherDaily: List<DailyWeatherResponse>) :
    RecyclerView.Adapter<WeatherDailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_weather_daily,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listWeatherDaily[position])
    }

    override fun getItemCount(): Int {
        return listWeatherDaily.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(dailyWeather: DailyWeatherResponse) {
            with(view) {
                tv_daily_time.text = dailyWeather.date.convertTimeStampToDate()
                tv_daily_temp.text = dailyWeather.temp.day.convertToCelcius()
                tv_daily_weather.text = dailyWeather.weather[0].main
            }
        }

    }
}