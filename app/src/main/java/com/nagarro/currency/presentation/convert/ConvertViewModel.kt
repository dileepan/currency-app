package com.nagarro.currency.presentation.convert

import androidx.core.text.isDigitsOnly
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.architecture.SingleLiveEvent
import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import com.nagarro.currency.listeners.OnItemSelectListener
import com.nagarro.currency.listeners.OnTextChangedListener
import com.nagarro.currency.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ConvertViewModel(private val useCase: CurrencyUseCase) : BaseViewModel() {

    private val _swapEvent = SingleLiveEvent<Pair<Int, Int>>()
    val swapEvent: LiveData<Pair<Int, Int>> = _swapEvent

    val fromCurrencyValue = ObservableField("")
    val toCurrencyValue = ObservableField("")

    val items: ObservableList<String> = ObservableArrayList()
    val itemBinding = ItemBinding.of<String>(BR.item, R.layout.item_spinner)

    val selectFromCurrency = object : OnItemSelectListener {
        override fun onItemSelected(position: Int) {
            if (from == items[position]) return
            from = items[position]
            fetchExchangeRate()
        }
    }

    val selectToCurrency = object : OnItemSelectListener {
        override fun onItemSelected(position: Int) {
            if (to == items[position]) return
            to = items[position]
            fetchExchangeRate()
        }
    }

    val onEnterFromCurrencyValue = object : OnTextChangedListener {
        override fun onTextChanged(value: String) {
            if (value.isBlank() || !value.isDecimalOnly()) return
            toCurrencyValue.set(String.format("%.2f", rate * value.toDouble()))
        }
    }

    val onEnterToCurrencyValue = object : OnTextChangedListener {
        override fun onTextChanged(value: String) {
            if (value.isBlank() || !value.isDecimalOnly()) return
            fromCurrencyValue.set(String.format("%.2f", value.toDouble() / rate))
        }
    }

    private var from = ""
    private var to = ""
    private var rate = 0.0

    fun init() {
        items.clear()
        viewModelScope.launch {
            showProgress(true)
            when (val res = useCase.fetchSymbols()) {
                is ResultState.Success -> {
                    items.addAll(res.data.keys.sorted())
                }
                is ResultState.Error -> setError(res.errorEntity)
            }
            showProgress(false)
        }
    }

    fun onDetailsClicked() {
        navigate(ConvertFragmentDirections.actionGoToDetailsScreen(from, to))
    }

    fun onSwapClicked() {
        if (to.isBlank() || from.isBlank()) return

        rate = 1.0 / rate
        val t = from
        from = to
        to = t

        if (fromCurrencyValue.get().isNullOrBlank() || !fromCurrencyValue.get().isDecimalOnly()) {
            fromCurrencyValue.set("1.00")
        }
        val toValue = fromCurrencyValue.get()?.let { it.toDouble() * rate } ?: rate
        toCurrencyValue.set(String.format("%.2f", toValue))
        _swapEvent.postValue(Pair(items.indexOf(from), items.indexOf(to)))
    }

    private fun fetchExchangeRate() {
        if (from.isBlank() || to.isBlank()) return
        if (from == to) {
            rate = 1.0
            if (fromCurrencyValue.get().isNullOrBlank() || !fromCurrencyValue.get().isDecimalOnly()) {
                fromCurrencyValue.set("1.00")
            }
            toCurrencyValue.set(fromCurrencyValue.get())
            return
        }
        viewModelScope.launch {
            showProgress(true)
            when (val res = useCase.fetchCurrentRate(from, to)) {
                is ResultState.Success -> {
                    rate = res.data
                    if (fromCurrencyValue.get().isNullOrBlank() || !fromCurrencyValue.get().isDecimalOnly()) {
                        fromCurrencyValue.set("1.00")
                    }
                    val toValue = fromCurrencyValue.get()?.let { it.toDouble() * rate } ?: rate
                    toCurrencyValue.set(String.format("%.2f", toValue))
                }
                is ResultState.Error -> setError(res.errorEntity)
            }
            showProgress(false)
        }
    }

    private fun String?.isDecimalOnly(): Boolean = this?.replace(".", "")?.isDigitsOnly() ?: false
}