package com.nagarro.currency.di

import com.nagarro.currency.data.remote.FixerApi
import com.nagarro.currency.data.repository.FixerRepositoryImpl
import com.nagarro.currency.domain.repository.FixerRepository
import com.nagarro.currency.domain.usecase.CurrencyUseCaseImpl
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<FixerApi> { get<Retrofit>().create(FixerApi::class.java) }

    single<FixerRepository> { FixerRepositoryImpl(get()) }

    single<CurrencyUseCase> { CurrencyUseCaseImpl(get()) }
}