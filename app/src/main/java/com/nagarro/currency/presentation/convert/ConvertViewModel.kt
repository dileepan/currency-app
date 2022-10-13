package com.nagarro.currency.presentation.convert

import androidx.lifecycle.MutableLiveData
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import com.nagarro.currency.presentation.base.BaseViewModel

class ConvertViewModel(private val useCase: CurrencyUseCase) : BaseViewModel() {

    val fromCurrencyValue = MutableLiveData("")
    val toCurrencyValue = MutableLiveData("")

    fun onEnterFromCurrencyValue(value: String) {

    }

    fun onEnterToCurrencyValue(value: String) {

    }

    fun onDetailsClicked() {

    }

    fun onSwapClicked() {

    }
}