package com.bassem.weathernow.fragments.currentWeather

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bassem.weathernow.R
import com.bassem.weathernow.api.models.apiCurrent.current_weather
import com.bassem.weathernow.databinding.TodayFragmentBinding
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class CurrentWeather : Fragment(R.layout.today_fragment) {
    var _binding: TodayFragmentBinding? = null
    val binding get() = _binding
    var viewModel: ViewModelCurrentWeather? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TodayFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModelCurrentWeather::class.java]
        viewModel!!.getLocation(requireContext())
        viewModel!!.currentWeather.observe(viewLifecycleOwner) {
            if (it != null) {
                updatingUI(it)
            } else {

            }
        }
        binding?.swipe?.setOnRefreshListener {
            viewModel!!.getLocation(requireContext())
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun updatingUI(currentWeather: current_weather) {
        val locale = Locale.US
        val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", locale)
        val timeFormater = DateTimeFormatter.ofPattern("hh:mm a", locale)

        binding?.TvcityName?.text = currentWeather.name
        binding?.TvCurrentTemp?.text = "${currentWeather.main.temp.toInt()} °C"
        val sunRiseSeconds: Int = currentWeather.sys.sunrise
        binding?.TvSunRise?.text =
            Instant.ofEpochSecond(sunRiseSeconds.toLong()).atZone(ZoneId.systemDefault())
                .format(timeFormater).toString()
        val sunSetSeconds: Int = currentWeather.sys.sunset
        binding?.TvSunSet?.text =
            Instant.ofEpochSecond(sunSetSeconds.toLong()).atZone(ZoneId.systemDefault())
                .format(timeFormater).toString()
        binding?.TvDescription?.text = currentWeather.weather[0].description
        binding?.TvWind?.text = "${currentWeather.wind.speed} Km/h"
        binding?.Tvhumidity?.text = "${currentWeather.main.humidity} %"
        binding?.TvPressure?.text = "${currentWeather.main.pressure}"
        binding?.TvFeel?.text = "${currentWeather.main.feels_like.toInt()} °C"
        val timeStamp = currentWeather.dt
        val formatedTime =
            Instant.ofEpochSecond(timeStamp.toLong()).atZone(ZoneId.systemDefault()).format(sdf)
        val displayedUpdate = formatedTime
        println(displayedUpdate)
        binding?.TvLastUpdate?.text = formatedTime.toString()
        val iconCode = currentWeather.weather[0].icon
        val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"
        Glide.with(this).load(iconUrl).into(binding?.imgIcon!!)
        binding?.shimmerEffect?.visibility = View.GONE
        binding?.currentLayout?.visibility = View.VISIBLE
        binding?.swipe?.isRefreshing = false

    }



}