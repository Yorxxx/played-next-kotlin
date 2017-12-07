package com.piticlistudio.playednext.domain.interactor.platform

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import com.piticlistudio.playednext.ui.PlatformUIUtils
import io.reactivex.Single
import javax.inject.Inject

class LoadPlatformsForGameUseCase @Inject constructor(private val platformRepository: PlatformRepository,
                                                      private val platformUIUtils: PlatformUIUtils) : SingleUseCaseWithParameter<Game, List<Platform>> {

    override fun execute(parameter: Game): Single<List<Platform>> {
        if (parameter.platforms != null && parameter.platforms!!.isNotEmpty()) {
            return Single.just(loadDisplayValuesForPlatforms(parameter.platforms!!))
        }
        return platformRepository.loadForGame(parameter.id)
                .map { loadDisplayValuesForPlatforms(it) }
    }

    private fun loadDisplayValuesForPlatforms(platforms: List<Platform>): List<Platform> {
        platforms.forEach {
            it.apply {
                displayColor = platformUIUtils.getColor(it.name)
                displayName = platformUIUtils.getAcronym(it.name)
            }
        }
        return platforms
    }
}