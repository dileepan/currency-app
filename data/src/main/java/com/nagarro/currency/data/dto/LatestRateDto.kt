package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class LatestRateDto(
    @SerializedName("base") val base: String? = null,
    @SerializedName("rates") val rates: HashMap<String, Double>? = null,
    @SerializedName("date") val date: Date? = null,
    @SerializedName("success") override val reqStatus: Boolean? = null,
    @SerializedName("timestamp") val timestamp: Long? = null,
    @SerializedName("error") override val errorDto: ErrorDto? = null
): BaseDto(reqStatus, errorDto)
