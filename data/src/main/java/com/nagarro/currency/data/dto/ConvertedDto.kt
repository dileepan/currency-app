package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName

data class ConvertedDto(
    @SerializedName("success") override val reqStatus: Boolean? = null,
    @SerializedName("info") val info: InfoDto? = null,
    @SerializedName("query") val query: QueryDto? = null,
    @SerializedName("result") val result: Double? = null,
    @SerializedName("error") override val errorDto: ErrorDto? = null
): BaseDto(reqStatus, errorDto) {
    class InfoDto(
        @SerializedName("rate") val rate: Double? = null,
        @SerializedName("timestamp") val timestamp: Long? = null
    )

    class QueryDto(
        @SerializedName("from") val fromCurrencyCode: String? = null,
        @SerializedName("to") val toCurrencyCode: String? = null,
        @SerializedName("amount") val amount: Double? = null
    )
}