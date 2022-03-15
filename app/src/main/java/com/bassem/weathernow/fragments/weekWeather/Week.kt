package com.bassem.weathernow.fragments.weekWeather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.weathernow.R
import com.bassem.weathernow.adapters.WeeklyAdapter
import com.bassem.weathernow.api.ServiceAPI
import com.bassem.weathernow.api.models.apiWeekly.Daily
import com.bassem.weathernow.api.models.apiWeekly.WeeklyxxWeather
import com.bassem.weathernow.databinding.WeekFragmentBinding

class Week : Fragment(R.layout.week_fragment) {
    var _binding: WeekFragmentBinding? = null
    val binding get() = _binding
    lateinit var lat: String
    lateinit var long: String
    lateinit var weeklyRV: RecyclerView
    lateinit var weatherList: MutableList<Daily>
    lateinit var hourlyAdpter: WeeklyAdapter
    val API_KEY = "b9eef1568d8d1cea6bb9549c7bda1bb9"
    var viewModel: ViewModelWeekWeather? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("PREF", Context.MODE_PRIVATE)
        lat = sharedPreferences?.getString("lat", "30.0444")!!
        long = sharedPreferences.getString("long", "31.2357")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WeekFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weeklyRV = view.findViewById(R.id.RvWeekly)
        weatherList = arrayListOf()
        viewModel = ViewModelProvider(this)[ViewModelWeekWeather::class.java]
        viewModel!!.getLastLocation()
        viewModel!!.weekList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                hourlyAdpter.addList(it)
            }
        })
        rvSetup()

    }

    fun rvSetup() {
        hourlyAdpter = WeeklyAdapter(weatherList, requireContext())
        weeklyRV.adapter = hourlyAdpter
        weeklyRV.setHasFixedSize(true)
        weeklyRV.layoutManager = LinearLayoutManager(context)
    }


}