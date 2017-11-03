package com.piticlistudio.playednext.domain.interactor.platform

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import io.reactivex.Single
import javax.inject.Inject

class LoadPlatformUseCase @Inject constructor(private val platformRepository: PlatformRepository) : SingleUseCaseWithParameter<Int, Platform> {

    override fun execute(parameter: Int): Single<Platform> {
        return platformRepository.load(parameter)
    }
}