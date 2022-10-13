package com.nagarro.currency.di

import com.nagarro.currency.presentation.convert.ConvertViewModel
import com.nagarro.currency.presentation.details.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { ConvertViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}