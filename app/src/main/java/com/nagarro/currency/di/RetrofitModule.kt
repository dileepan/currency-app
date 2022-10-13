package com.nagarro.currency.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nagarro.currency.BuildConfig
import com.nagarro.currency.data.constants.FIXER_BASE_URL
import com.nagarro.currency.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<HeaderInterceptor>())
            .build()

        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(getGsonConverterFactory())
            .baseUrl(FIXER_BASE_URL)
            .build()
    }

    single {
        HeaderInterceptor()
    }

    single {
        HttpLoggingInterceptor().apply {
            this.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}

fun getGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create(getGson())
}

fun getGson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.setDateFormat("yyyy-MM-dd")
    return gsonBuilder.create()
}