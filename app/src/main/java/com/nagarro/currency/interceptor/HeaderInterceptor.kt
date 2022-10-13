package com.nagarro.currency.interceptor

import com.nagarro.currency.data.constants.FIXER_API_KEY
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response?
        var requestBuilder: Request.Builder = chain.request()
            .newBuilder()
            .header("apikey", FIXER_API_KEY)
        val request = requestBuilder.build()
        Timber.v("Headers: ... %s", request.headers.toString())
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Timber.e(e)
            throw e
        }
        return response
    }

}