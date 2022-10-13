package com.nagarro.currency.domain.entity

sealed class DataEntity {

    abstract val base: String

    data class HistoricalData(
        override val base: String,
        val to: String,
        val dateRateMap: Map<String, Double>
    ): DataEntity()

    data class LatestData(
        override val base: String,
        val symbolRateMap: Map<String, Double>
    ): DataEntity()
}
