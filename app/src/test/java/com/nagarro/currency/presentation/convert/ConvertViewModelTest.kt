package com.nagarro.currency.presentation.convert

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nagarro.currency.data.remote.FixerApi
import com.nagarro.currency.data.repository.FixerRepositoryImpl
import com.nagarro.currency.domain.repository.FixerRepository
import com.nagarro.currency.domain.usecase.CurrencyUseCase
import com.nagarro.currency.domain.usecase.CurrencyUseCaseImpl
import com.nagarro.currency.util.MainDispatcherRule
import com.nagarro.currency.util.symbolsResponse
import com.nagarro.currency.util.latestAedResponse
import com.nagarro.currency.util.latestInrResponse
import com.nagarro.currency.util.timeseriesResponse
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ConvertViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var convertViewModel: ConvertViewModel

    private lateinit var useCase: CurrencyUseCase

    private lateinit var repository: FixerRepository

    @MockK
    private lateinit var api: FixerApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockkStatic(TextUtils::class)
        repository = FixerRepositoryImpl(api)
        useCase = CurrencyUseCaseImpl(repository)
        convertViewModel = ConvertViewModel(useCase)

        every { TextUtils.isDigitsOnly(any()) } returns true

        coEvery { api.getSymbols() } returns Response.success(200, symbolsResponse)
        coEvery { api.getLatestExchangeRate("AED", any()) } returns Response.success(200, latestAedResponse)
        coEvery { api.getLatestExchangeRate("INR", any()) } returns Response.success(200, latestInrResponse)
        coEvery { api.getTimeseries(any(), any(), any(), any()) } returns Response.success(200, timeseriesResponse)

    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun init() {
        runTest {
            convertViewModel.init()
            assert(convertViewModel.items.size > 0)
        }
    }

    @Test
    fun onEnterFromCurrencyValue() {
        runTest {
            convertViewModel.init()
            convertViewModel.selectFromCurrency.onItemSelected(0)
            convertViewModel.selectToCurrency.onItemSelected(1)
            convertViewModel.onEnterFromCurrencyValue.onTextChanged("2")
            assertEquals(convertViewModel.toCurrencyValue.get(), "44.76")
        }
    }

    @Test
    fun onEnterToCurrencyValue() {
        runTest {
            convertViewModel.init()
            convertViewModel.selectFromCurrency.onItemSelected(0)
            convertViewModel.selectToCurrency.onItemSelected(1)
            convertViewModel.onEnterToCurrencyValue.onTextChanged("44")
            assertEquals(convertViewModel.fromCurrencyValue.get(), "1.97")
        }
    }

    @Test
    fun onSwapClicked() {
        runTest {
            convertViewModel.init()
            convertViewModel.selectFromCurrency.onItemSelected(0)
            convertViewModel.selectToCurrency.onItemSelected(1)
            convertViewModel.onSwapClicked()
            assertEquals(convertViewModel.toCurrencyValue.get(), "0.04")
        }
    }
}