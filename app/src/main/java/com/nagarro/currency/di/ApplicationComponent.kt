package com.nagarro.currency.di

import org.koin.core.module.Module

val appComponent: List<Module> = listOf(appModule, retrofitModule, vmModule)