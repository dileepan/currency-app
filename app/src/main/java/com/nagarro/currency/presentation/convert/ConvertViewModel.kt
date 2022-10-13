package com.nagarro.currency.presentation.convert

import android.text.Editable
import androidx.core.text.isDigitsOnly
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import com.nagarro.currency.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ConvertViewModel(private val useCase: CurrencyUseCase) : BaseViewModel() {

    val fromCurrencyValue = ObservableField("")
    val toCurrencyValue = ObservableField("")
    val fromCurr = ObservableField("")
    val toCurr = ObservableField("")

    val items: ObservableList<String> = ObservableArrayList()
    val itemBinding = ItemBinding.of<String>(BR.item, R.layout.item_spinner)

    private var rate: Double = 0.0
    private var from = ""
    private var to = ""

    init {
        viewModelScope.launch {
            showProgress(true)
            when (val res = useCase.fetchSymbols()) {
                is ResultState.Success -> {
                    items.addAll(res.data.keys)
                }
                is ResultState.Error -> setError(res.errorEntity)
            }
            showProgress(false)
        }
    }

    fun onEnterFromCurrencyValue(value: Editable) {
        toCurrencyValue.set(String.format("%.2f", rate * value.toString().toDouble()))
    }

    fun onEnterToCurrencyValue(value: Editable) {
        fromCurrencyValue.set(String.format("%.2f", value.toString().toDouble() / rate))
    }

    fun onSelectFromCurrency(pos: Int) {
        from = items[pos]
        if (fromCurr.get() == from) return
        fromCurr.set(from)
        fetchExchangeRate()
    }

    fun onSelectToCurrency(pos: Int) {
        to = items[pos]
        if (toCurr.get() == to) return
        toCurr.set(to)
        fetchExchangeRate()
    }

    fun onDetailsClicked() {
        navigate(ConvertFragmentDirections.actionGoToDetailsScreen())
    }

    fun onSwapClicked() {
        if (to.isBlank() || from.isBlank()) return
        rate = 1.0 / rate
        toCurr.set(from)
        fromCurr.set(to)
        from = fromCurr.get() ?: ""
        to = toCurr.get() ?: ""
        if (fromCurrencyValue.get().isNullOrBlank() || fromCurrencyValue.get()?.isDigitsOnly() != true) {
            fromCurrencyValue.set("1")
        }
        val toValue = fromCurrencyValue.get()?.let { it.toDouble() * rate } ?: rate
        toCurrencyValue.set(String.format("%.2f", toValue))
    }

    private fun fetchExchangeRate() {
        if (from.isBlank() || to.isBlank()) return
        if (from == to) {
            rate = 1.0
            if (fromCurrencyValue.get().isNullOrBlank() || fromCurrencyValue.get()?.isDigitsOnly() != true) {
                fromCurrencyValue.set("1")
            }
            toCurrencyValue.set(fromCurrencyValue.get())

        }
        viewModelScope.launch {
            showProgress(true)
            when (val res = useCase.fetchCurrentRate(from, to)) {
                is ResultState.Success -> {
                    rate = res.data
                    if (fromCurrencyValue.get().isNullOrBlank() || fromCurrencyValue.get()?.isDigitsOnly() != true) {
                        fromCurrencyValue.set("1")
                    }
                    val toValue = fromCurrencyValue.get()?.let { it.toDouble() * rate } ?: rate
                    toCurrencyValue.set(String.format("%.2f", toValue))
                }
                is ResultState.Error -> setError(res.errorEntity)
            }
            showProgress(false)
        }
    }
}