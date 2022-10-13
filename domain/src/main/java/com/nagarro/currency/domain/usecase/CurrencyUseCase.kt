package com.nagarro.currency.domain.usecase

import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.entity.DataEntity

interface CurrencyUseCase {

    suspend fun fetchSymbols(): ResultState<HashMap<String, String>>

    suspend fun fetchHistoricalData(base: String, to: String): ResultState<DataEntity.HistoricalData>

    suspend fun fetchLatestData(base: String): ResultState<DataEntity.LatestData>

    suspend fun fetchCurrentRate(base: String, to: String): ResultState<Double>
}