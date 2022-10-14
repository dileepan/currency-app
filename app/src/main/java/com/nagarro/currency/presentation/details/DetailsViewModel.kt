package com.nagarro.currency.presentation.details

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.data.constants.FIXER_DATE_FORMAT
import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import com.nagarro.currency.presentation.base.BaseViewModel
import com.nagarro.currency.presentation.model.HistoricalModel
import com.nagarro.currency.presentation.model.LatestModel
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.text.SimpleDateFormat

class DetailsViewModel(private val currencyUseCase: CurrencyUseCase) : BaseViewModel() {

    val baseCurrency = ObservableField("")
    val toCurrency = ObservableField("")

    val historicalItems = ObservableArrayList<HistoricalModel>()
    val latestItems = ObservableArrayList<LatestModel>()
    val historicalItemBinding = ItemBinding.of<HistoricalModel>(BR.item, R.layout.item_historical_rate)
    val latestItemBinding = ItemBinding.of<LatestModel>(BR.item, R.layout.item_latest_rate)

    fun loadData(from: String, to: String) {
        baseCurrency.set(from)
        toCurrency.set(to)
        if (latestItems.isNotEmpty() && historicalItems.isNotEmpty()) return
        viewModelScope.launch {
            showProgress(true)
            val historical = currencyUseCase.fetchHistoricalData(from, to)
            val latest = currencyUseCase.fetchLatestData(from)
            when (historical) {
                is ResultState.Success -> historicalItems.addAll(
                    historical.data.dateRateMap.map { HistoricalModel(it.key, it.value) }
                        .sortedByDescending { SimpleDateFormat(FIXER_DATE_FORMAT).parse(it.date) }
                )
                is ResultState.Error -> setError(historical.errorEntity)
            }
            when (latest) {
                is ResultState.Success -> latestItems.addAll(latest.data.symbolRateMap.map { LatestModel(it.key, it.value) })
                is ResultState.Error -> setError(latest.errorEntity)
            }
            showProgress(false)
        }
    }
}