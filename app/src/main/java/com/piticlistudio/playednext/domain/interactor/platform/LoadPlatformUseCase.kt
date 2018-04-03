package com.piticlistudio.playednext.domain.interactor.platform

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import com.piticlistudio.playednext.ui.PlatformUIUtils
import io.reactivex.Single
import javax.inject.Inject

class LoadPlatformUseCase @Inject constructor(private val platformRepository: PlatformRepository,
                                              private val platformUIUtils: PlatformUIUtils) : SingleUseCaseWithParameter<Int, Platform> {

    override fun execute(parameter: Int): Single<Platform> {
        return platformRepository.load(parameter)
                .map {
                    it.apply {
                        displayColor = platformUIUtils.getColor(it.name)
                        displayName = platformUIUtils.getAcronym(it.name)
                    }
                }
    }
}