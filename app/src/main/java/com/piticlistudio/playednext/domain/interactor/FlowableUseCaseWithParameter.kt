package com.piticlistudio.playednext.domain.interactor

import io.reactivex.Flowable

interface FlowableUseCaseWithParameter<P, R> {

    fun execute(parameter: P): Flowable<R>
}