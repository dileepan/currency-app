package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TimeseriesDto(
    @SerializedName("base") val base: String? = null,
    @SerializedName("end_date") val endDate: Date? = null,
    @SerializedName("start_date") val startDate: Date? = null,
    @SerializedName("rates") val rates: HashMap<String, HashMap<String, Double>>? = null,
    @SerializedName("success") override val success: Boolean? = null,
    @SerializedName("error") override val error: ErrorDto? = null
): BaseDto(success, error)
