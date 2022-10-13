package com.nagarro.currency.domain.common

sealed class ErrorEntity {
    class Error(val code: Int, val message: String) : ErrorEntity()
}