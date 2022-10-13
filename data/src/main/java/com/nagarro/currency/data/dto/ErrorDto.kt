package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName

data class ErrorDto(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("info") val message: String? = null,
    @SerializedName("type") val type: String? = null
)
