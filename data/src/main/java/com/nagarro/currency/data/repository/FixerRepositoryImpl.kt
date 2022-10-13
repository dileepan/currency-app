package com.nagarro.currency.data.repository

import com.nagarro.currency.data.remote.FixerApi
import com.nagarro.currency.domain.repository.FixerRepository

class FixerRepositoryImpl(val api: FixerApi) : FixerRepository {


}