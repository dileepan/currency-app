package com.nagarro.currency.di

import org.koin.core.module.Module

val appComponent: List<Module> = listOf(retrofitModule, appModule, vmModule)