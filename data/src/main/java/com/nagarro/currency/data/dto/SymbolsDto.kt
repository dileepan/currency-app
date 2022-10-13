package com.nagarro.currency.data.dto

import com.google.gson.annotations.SerializedName

data class SymbolsDto(
    @SerializedName("success") override val success: Boolean? = null,
    @SerializedName("symbols") val symbols: HashMap<String, String>? = null,
    @SerializedName("error") override val error: ErrorDto? = null
): BaseDto(success, error)
