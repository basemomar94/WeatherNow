package com.bassem.weathernow.fragments.currentWeather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bassem.weathernow.api.ServiceAPI
import com.bassem.weathernow.api.models.apiCurrent.current_weather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCurrentWeather(application: Application) :
    AndroidViewModel(application) {


    lateinit var fusedlocation: FusedLocationProviderClient
    private var lat: String? = null
    private var long: String? = null
    val API_KEY = "b9eef1568d8d1cea6bb9549c7bda1bb9"
    val currentWeather = MutableLiveData<current_weather>()


    @SuppressLint("MissingPermission")
    fun getLocation(context: Context) {
        fusedlocation = LocationServices.getFusedLocationProviderClient(context)
        fusedlocation.lastLocation.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude.toString()
                long = it.longitude.toString()
                saveLocation(lat!!, long!!)
                getCurrentWeather(lat!!, long!!)
            } else {
                getCurrentWeather(getLastLocation().first, getLastLocation().second)
            }
        }

    }

    fun getCurrentWeather(valueLat: String, valueLong: String) {
        val api = ServiceAPI.create()
            .currentWeather("weather", valueLat, valueLong, API_KEY, "metric")
        api.enqueue(object : Callback<current_weather?> {
            override fun onResponse(
                call: Call<current_weather?>,
                response: Response<current_weather?>
            ) {
                currentWeather.postValue(response.body())
                println("=====================${response.code()}")
            }

            override fun onFailure(call: Call<current_weather?>, t: Throwable) {
               println("${t.message}     Fai")
            }
        })
    }

    fun saveLocation(lat: String, long: String) {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("lat", lat)
        editor?.putString("long", long)
        editor?.apply()
    }

    fun getLastLocation(): Pair<String, String> {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getString("lat", "30")
        val long = sharedPreferences.getString("long", "33")

        return Pair(lat!!, long!!)

    }


}