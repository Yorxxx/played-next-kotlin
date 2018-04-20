package com.piticlistudio.playednext.domain.interactor

import io.reactivex.Flowable

interface FlowableUseCase<R> {

    fun execute(): Flowable<R>
}