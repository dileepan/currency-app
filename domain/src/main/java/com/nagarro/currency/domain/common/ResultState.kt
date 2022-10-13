package com.nagarro.currency.domain.common

import com.nagarro.currency.domain.entity.ErrorEntity

sealed class ResultState<T> {
    data class Success<T>(val data: T): ResultState<T>()
    data class Error<T>(val errorEntity: ErrorEntity.Error): ResultState<T>()
}
