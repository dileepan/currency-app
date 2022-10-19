package com.nagarro.currency.util

import com.nagarro.currency.data.dto.LatestRateDto
import com.nagarro.currency.data.dto.SymbolsDto
import com.nagarro.currency.data.dto.TimeseriesDto
import java.text.SimpleDateFormat

val symbolsResponse = SymbolsDto(
    reqStatus = true,
    symbols = hashMapOf(
        ("AED" to "United Arab Emirates Dirham"),
        ("INR" to "Indian Rupee")
    )
)

val latestAedResponse = LatestRateDto(
    base = "AED",
    date = SimpleDateFormat("yyyy-MM-dd").parse("2022-10-17"),
    rates = hashMapOf(("INR" to 22.377794)),
    reqStatus = true,
    timestamp = 1666011843
)

val latestInrResponse = LatestRateDto(
    base = "INR",
    date = SimpleDateFormat("yyyy-MM-dd").parse("2022-10-17"),
    rates = hashMapOf(("AED" to 0.044687)),
    reqStatus = true,
    timestamp = 1666011843
)

val timeseriesResponse = TimeseriesDto(
    base = "AED",
    endDate = SimpleDateFormat("yyyy-MM-dd").parse("2022-10-17"),
    startDate = SimpleDateFormat("yyyy-MM-dd").parse("2022-10-15"),
    rates = hashMapOf(
        "2022-10-15" to hashMapOf(("INR" to 22.438324)),
        "2022-10-16" to hashMapOf(("INR" to 22.437378)),
        "2022-10-17" to hashMapOf(("INR" to 22.372077))
    ),
    reqStatus = true
)