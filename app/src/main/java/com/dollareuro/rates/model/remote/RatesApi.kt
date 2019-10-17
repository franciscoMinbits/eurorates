package com.dollareuro.rates.model.remote

import com.dollareuro.rates.model.data.RatesResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RatesApi {
    @GET("latest")
    fun getRates(): Deferred<RatesResult>
}