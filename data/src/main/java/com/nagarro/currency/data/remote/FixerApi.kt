package com.nagarro.currency.data.remote

import retrofit2.http.GET

interface FixerApi {

    @GET("fixer/symbols")
    suspend fun getSymbols()

    @GET("fixer/latest")
    suspend fun getLatestExchangeRate()

    @GET("fixer/{start_date}")
    suspend fun getHistoricalRates()

    @GET("fixer/timeseries")
    suspend fun getTimeseries()
}