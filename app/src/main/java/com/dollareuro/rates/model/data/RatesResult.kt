package com.dollareuro.rates.model.data

data class RatesResult(
    val success: Boolean,
    val timestamp: String? = null,
    val base: String? = null,
    val date: String? = null,
    val rates: MutableMap<String, Double>? = null
)
