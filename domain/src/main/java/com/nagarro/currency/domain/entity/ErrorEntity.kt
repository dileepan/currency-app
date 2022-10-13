package com.nagarro.currency.domain.entity

sealed class ErrorEntity {
    class Error(val code: Int, val message: String) : ErrorEntity()
}