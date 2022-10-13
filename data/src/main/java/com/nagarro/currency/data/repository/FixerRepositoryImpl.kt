package com.nagarro.currency.data.repository

import android.util.MalformedJsonException
import com.nagarro.currency.data.constants.NetworkConstants
import com.nagarro.currency.data.dto.BaseDto
import com.nagarro.currency.data.mapper.map
import com.nagarro.currency.data.remote.FixerApi
import com.nagarro.currency.domain.entity.ErrorEntity
import com.nagarro.currency.domain.common.ResultState
import com.nagarro.currency.domain.entity.DataEntity
import com.nagarro.currency.domain.repository.FixerRepository
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.Calendar

class FixerRepositoryImpl(val api: FixerApi) : FixerRepository {

    private val logFormatter: String = "%s | %s"

    override suspend fun fetchSymbols(): ResultState<HashMap<String, String>> {
        return when(val res = apiCall { api.getSymbols() }) {
            is ResultState.Success -> ResultState.Success(res.data.symbols ?: hashMapOf())
            is ResultState.Error -> ResultState.Error(res.errorEntity)
        }
    }

    override suspend fun fetchLatestData(base: String): ResultState<DataEntity.LatestData> {
        val top10Symbols = mutableListOf("USD", "GBP", "EUR", "AUD", "JPY", "CHF", "CAD", "CNH", "HKD", "NZD")
        if (top10Symbols.contains(base)) top10Symbols[top10Symbols.indexOf(base)] = "INR"
        return when(val res = apiCall { api.getLatestExchangeRate(base, top10Symbols.joinToString(",")) }) {
            is ResultState.Success -> ResultState.Success(res.data.map())
            is ResultState.Error -> ResultState.Error(res.errorEntity)
        }
    }

    override suspend fun fetchHistoricalData(base: String, to: String): ResultState<DataEntity.HistoricalData> {
        val cal = Calendar.getInstance()
        val endDate = cal.time
        cal.add(Calendar.DATE, 3)
        val startDate = cal.time
        return when(val res = apiCall { api.getTimeseries(startDate, endDate, base, to) }) {
            is ResultState.Success -> ResultState.Success(res.data.map())
            is ResultState.Error -> ResultState.Error(res.errorEntity)
        }
    }

    override suspend fun fetchCurrentRate(base: String, to: String): ResultState<Double> {
        return when(val res = apiCall { api.getLatestExchangeRate(base, to) }) {
            is ResultState.Success -> ResultState.Success(res.data.rates?.get(to) ?: 0.0)
            is ResultState.Error -> ResultState.Error(res.errorEntity)
        }
    }

    private suspend fun <T: BaseDto> apiCall(call: suspend () -> Response<out T>): ResultState<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { r ->
                    if (r.success == true) ResultState.Success(r)
                    else r.error?.let { errorDto ->  ResultState.Error(errorDto.map()) }
                } ?: ResultState.Error(
                    ErrorEntity.Error(
                        NetworkConstants.NETWORK_ERROR_CODES.UNEXPECTED_ERROR,
                        NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR
                    )
                )
            } else ResultState.Error(
                ErrorEntity.Error(
                    NetworkConstants.NETWORK_ERROR_CODES.UNEXPECTED_ERROR,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR
                )
            )
        } catch (ex: Throwable) {
            ResultState.Error(handleCommonErrors(ex))
        }
    }

    private fun handleCommonErrors(throwable: Throwable): ErrorEntity.Error {
        return when (throwable) {
            is SocketTimeoutException, is SocketException, is InterruptedIOException -> {
                Timber.e(
                    logFormatter,
                    throwable.message.toString(),
                    NetworkConstants.NETWORK_ERROR_MESSAGES.SERVICE_UNAVAILABLE
                )

                ErrorEntity.Error(
                    NetworkConstants.NETWORK_ERROR_CODES.SERVICE_UNAVAILABLE,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.SERVICE_UNAVAILABLE
                )
            }

            is MalformedJsonException -> {
                Timber.e(
                    logFormatter,
                    throwable.message.toString(),
                    NetworkConstants.NETWORK_ERROR_MESSAGES.MALFORMED_JSON
                )

                ErrorEntity.Error(
                    NetworkConstants.NETWORK_ERROR_CODES.MALFORMED_JSON,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.MALFORMED_JSON
                )
            }
            is IOException -> {
                Timber.e(
                    logFormatter,
                    throwable.message.toString(),
                    NetworkConstants.NETWORK_ERROR_MESSAGES.NO_INTERNET
                )

                ErrorEntity.Error(
                    NetworkConstants.NETWORK_ERROR_CODES.NO_INTERNET,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.NO_INTERNET
                )
            }

            is HttpException -> {
                Timber.e(
                    logFormatter,
                    throwable.response()?.toString() ?: throwable.message().toString(),
                    ""
                )
                ErrorEntity.Error(
                    throwable.code(),
                    throwable.message()
                )
            }
            else -> {
                Timber.e(
                    logFormatter, NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR
                )

                ErrorEntity.Error(
                    NetworkConstants.NETWORK_ERROR_CODES.UNEXPECTED_ERROR,
                    NetworkConstants.NETWORK_ERROR_MESSAGES.UNEXPECTED_ERROR
                )
            }
        }
    }
}