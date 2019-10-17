package com.dollareuro.rates.model.repository

import com.dollareuro.rates.model.data.RatesResult
import com.dollareuro.rates.model.remote.RatesApi

interface RatesRepository {
    suspend fun getRatesList(): UseCaseResult<RatesResult>
}

class RatesRepositoryImpl(private val api: RatesApi) : RatesRepository {
    override suspend fun getRatesList(): UseCaseResult<RatesResult> {
        return try {
            val result = api.getRates().await()
            UseCaseResult.Success(result)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}

sealed class UseCaseResult<out T : Any> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>()
    class Error(val exception: Throwable) : UseCaseResult<Nothing>()
}