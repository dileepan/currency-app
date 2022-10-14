package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap

data class HistoricalDto(
    @SerializedName("base") val base: String? = null,
    @SerializedName("date") val date: Date? = null,
    @SerializedName("rates") val rates: HashMap<String, Double>? = null,
    @SerializedName("success") override val reqStatus: Boolean? = null,
    @SerializedName("error") override val errorDto: ErrorDto? = null
): BaseDto(reqStatus, errorDto)
