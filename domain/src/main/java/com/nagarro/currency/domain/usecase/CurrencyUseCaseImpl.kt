package com.nagarro.currency.domain.usecase

import com.nagarro.currency.domain.repository.FixerRepository

class CurrencyUseCaseImpl(val repository: FixerRepository) : CurrencyUseCase {

}