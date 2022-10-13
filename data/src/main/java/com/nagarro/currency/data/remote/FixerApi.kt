package com.nagarro.currency.data.remote

import com.nagarro.currency.data.dto.HistoricalDto
import com.nagarro.currency.data.dto.LatestRateDto
import com.nagarro.currency.data.dto.SymbolsDto
import com.nagarro.currency.data.dto.TimeseriesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface FixerApi {

    @GET("fixer/symbols")
    suspend fun getSymbols(): Response<SymbolsDto>

    @GET("fixer/latest")
    suspend fun getLatestExchangeRate(
        @Query("base") baseSymbol: String,
        @Query("symbols") currencySymbols: String
    ): Response<LatestRateDto>

    @GET("fixer/{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: Date,
        @Query("base") base: String,
        @Query("symbols") currencySymbols: String
    ): Response<HistoricalDto>

    @GET("fixer/timeseries")
    suspend fun getTimeseries(
        @Query("start_date") startDate: Date,
        @Query("end_date") endDate: Date,
        @Query("base") base: String,
        @Query("symbols") currencySymbols: String
    ): Response<TimeseriesDto>
}