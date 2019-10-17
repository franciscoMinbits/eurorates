package com.dollareuro.rates.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dollareuro.rates.R
import com.dollareuro.rates.view.model.RatesViewModel
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: RatesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initModel()
    }

    fun initModel() {
        viewModel.ratesList.observe(this, Observer { ratesList ->
            val lineData = BarData(ratesList)
            ratesChart.data = lineData
            ratesChart.invalidate()

        })

        viewModel.showLoading.observe(this, Observer { showLoading ->
            progressBar.visibility = if (showLoading!!) View.VISIBLE else View.GONE
        })

        viewModel.showError.observe(this, Observer { showError ->
            Toast.makeText(this, showError, Toast.LENGTH_SHORT).show()
        })
    }
}
