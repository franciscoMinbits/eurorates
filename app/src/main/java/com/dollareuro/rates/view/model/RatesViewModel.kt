package com.dollareuro.rates.view.model

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.dollareuro.rates.model.repository.RatesRepository
import com.dollareuro.rates.model.repository.UseCaseResult
import com.dollareuro.rates.module.SingleLiveEvent
import com.dollareuro.rates.view.base.BaseViewModel
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class RatesViewModel(private val ratesRepository: RatesRepository) : BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    val ratesList = MutableLiveData<List<BarDataSet>>()
    val showError = SingleLiveEvent<String>()

    init {
        loadRates()
    }

    private fun loadRates() {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.IO) { ratesRepository.getRatesList() }
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success -> {
                    var dataSets = ArrayList<BarDataSet>()
                    result.data.rates?.let {
                        var i = 0F
                        it.forEach { (key, value) ->
                            if (i > 10)
                                return@forEach
                            if (value.toFloat() <= 20.00) {
                                val entry = BarEntry(i, value.toFloat())
                                val listEntry: List<BarEntry> = arrayListOf(entry)
                                val dataSet = BarDataSet( listEntry, key)
                                val rnd = Random()
                                dataSet.color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                                dataSets.add(dataSet)
                                i += 1
                            }
                        }
                    }
                    ratesList.value = dataSets
                }
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
    }
}
