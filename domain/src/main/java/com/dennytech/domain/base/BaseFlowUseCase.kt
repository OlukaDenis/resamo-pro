package com.dennytech.domain.base

import com.dennytech.domain.dispacher.AppDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseFlowUseCase<in Param, Result>(
    private val dispatcher: AppDispatcher
) where Param : Any {

    abstract fun run(param: Param?): Flow<Result>
     operator fun invoke(param: Param? = null) = run(param).flowOn(dispatcher.io)
}