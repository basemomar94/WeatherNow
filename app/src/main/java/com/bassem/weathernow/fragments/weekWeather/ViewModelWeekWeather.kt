package com.bassem.weathernow.fragments.weekWeather

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bassem.weathernow.api.ServiceAPI
import com.bassem.weathernow.api.models.apiHourly.Hourly
import com.bassem.weathernow.api.models.apiWeekly.Daily
import com.bassem.weathernow.api.models.apiWeekly.WeeklyxxWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelWeekWeather(app: Application) : AndroidViewModel(app) {
    val API_KEY = "b9eef1568d8d1cea6bb9549c7bda1bb9"
    val weekList = MutableLiveData<List<Daily>>()

    fun getLastLocation() {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getString("lat", "30")
        val long = sharedPreferences.getString("long", "33")

        getWeek(lat!!, long!!)

    }


    fun getWeek(lat: String, long: String) {

        val api =
            ServiceAPI.create().weeklyWeather(lat, long, "minutely,hourly", API_KEY, "7", "metric")
        api.enqueue(object : Callback<WeeklyxxWeather?> {
            override fun onResponse(
                call: Call<WeeklyxxWeather?>,
                response: Response<WeeklyxxWeather?>
            ) {
                if (response.isSuccessful) {
                    if (response != null) {
                        weekList.postValue(response.body()!!.daily)
                    }
                }
            }

            override fun onFailure(call: Call<WeeklyxxWeather?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }


}