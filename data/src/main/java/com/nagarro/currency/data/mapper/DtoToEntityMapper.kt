package com.nagarro.currency.data.mapper

import com.nagarro.currency.data.constants.NetworkConstants
import com.nagarro.currency.data.dto.ErrorDto
import com.nagarro.currency.data.dto.LatestRateDto
import com.nagarro.currency.data.dto.TimeseriesDto
import com.nagarro.currency.domain.entity.DataEntity
import com.nagarro.currency.domain.entity.ErrorEntity

fun ErrorDto.map() = ErrorEntity.Error(
    code = this.code ?: NetworkConstants.NETWORK_ERROR_CODES.UNEXPECTED_ERROR,
    message = this.message ?: this.type ?: NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR
)

fun TimeseriesDto.map() = DataEntity.HistoricalData(
    base = this.base ?: "",
    to = this.rates?.values?.first()?.keys?.first() ?: "",
    dateRateMap = this.rates?.let {
        val map = mutableMapOf<String, Double>()
        it.forEach { entry -> map.put(entry.key, entry.value.values.first()) }
        map
    } ?: hashMapOf()
)

fun LatestRateDto.map() = DataEntity.LatestData(
    base = this.base ?: "",
    symbolRateMap = this.rates ?: mapOf()
)