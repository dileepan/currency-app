package com.nagarro.currency.domain.usecase

import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.entity.DataEntity
import com.nagarro.currency.domain.repository.FixerRepository

class CurrencyUseCaseImpl(val repository: FixerRepository) : CurrencyUseCase {

    override suspend fun fetchHistoricalData(
        base: String,
        to: String
    ): ResultState<DataEntity.HistoricalData> {
        return repository.fetchHistoricalData(base, to)
    }

    override suspend fun fetchLatestData(base: String): ResultState<DataEntity.LatestData> {
        return repository.fetchLatestData(base)
    }

    override suspend fun fetchSymbols(): ResultState<HashMap<String, String>> {
        return repository.fetchSymbols()
    }

    override suspend fun fetchCurrentRate(base: String, to: String): ResultState<Double> {
        return repository.fetchCurrentRate(base, to)
    }
}