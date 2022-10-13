package com.nagarro.currency.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.nagarro.currency.architecture.NavigationCommand
import com.nagarro.currency.architecture.SingleLiveEvent
import com.nagarro.currency.domain.common.ErrorEntity

abstract class BaseViewModel : ViewModel() {

    private val _progressObserver = MutableLiveData<Boolean>()
    val progressObserver: LiveData<Boolean> = _progressObserver

    private val _navigateEvent = SingleLiveEvent<NavigationCommand>()
    val navigateEvent: LiveData<NavigationCommand> = _navigateEvent

    private val _errorEvent = SingleLiveEvent<ErrorEntity.Error>()
    val errorEvent: LiveData<ErrorEntity.Error> = _errorEvent

    fun setError(error: ErrorEntity.Error) {
        _errorEvent.value = error
    }

    fun setError(errorMessage: String) {
        _errorEvent.value = ErrorEntity.Error(0, errorMessage)
    }

    fun showProgress(show: Boolean) = _progressObserver.postValue(show)

    fun navigate(directions: NavDirections) =
        _navigateEvent.postValue(NavigationCommand.To(directions))

    fun navigateBack() = _navigateEvent.postValue(NavigationCommand.Back)

    fun popTo(destinationId: Int, inclusive: Boolean = false) =
        _navigateEvent.postValue(NavigationCommand.BackTo(destinationId, inclusive))
}