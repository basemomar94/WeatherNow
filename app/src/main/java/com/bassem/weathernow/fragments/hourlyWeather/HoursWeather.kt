package com.bassem.weathernow.fragments.hourlyWeather

import android.annotation.SuppressLint
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
import com.bassem.weathernow.adapters.HourlyAdapter
import com.bassem.weathernow.api.ServiceAPI
import com.bassem.weathernow.api.models.apiHourly.Hourly
import com.bassem.weathernow.api.models.apiHourly.HourlyWeather
import com.bassem.weathernow.databinding.HoursFragmentBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class HoursWeather : Fragment(R.layout.hours_fragment) {

    var _binding: HoursFragmentBinding? = null
    val binding get() = _binding
    lateinit var lat: String
    lateinit var long: String
    lateinit var hourlyRV: RecyclerView
    lateinit var weatherList: MutableList<Hourly>
    lateinit var hourlyAdpter: HourlyAdapter
    var viewModel: ViewModelHoursWeather? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HoursFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourlyRV = view.findViewById(R.id.hourlyRv)
        weatherList = arrayListOf()
        rvSetup()
        viewModel = ViewModelProvider(this)[ViewModelHoursWeather::class.java]
        viewModel!!.getLastLocation()
        viewModel!!.hoursList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                hourlyAdpter.addList(it)
                binding?.hourlyShimmer?.visibility = View.GONE
                binding?.hourlyLayout?.visibility = View.VISIBLE
            }


        })

    }




    @SuppressLint("UseRequireInsteadOfGet")
    fun rvSetup() {
        hourlyAdpter = HourlyAdapter(weatherList, requireContext())
        hourlyRV.apply {
            adapter = hourlyAdpter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }
}

