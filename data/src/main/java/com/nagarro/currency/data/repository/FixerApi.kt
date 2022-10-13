package com.nagarro.currency.data.repository

import retrofit2.http.GET

interface FixerApi {

    @GET("api/symbols")
    suspend fun getSymbols()

    @GET("api/latest")
    suspend fun getLatestExchangeRate()

    @GET("api/{start_date}")
    suspend fun getHistoricalRates()

    @GET("api/timeseries")
    suspend fun getTimeseries()
}