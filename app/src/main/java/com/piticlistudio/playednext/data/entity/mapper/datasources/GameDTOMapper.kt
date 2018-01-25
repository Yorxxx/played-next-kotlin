package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.net.*
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class GameDTOMapper @Inject constructor(private val companyDTOMapper: DTOModelMapper<CompanyDTO, Company>,
                                        private val genreDTOMapper: DTOModelMapper<GenreDTO, Genre>,
                                        private val collectionDTOMapper: DTOModelMapper<CollectionDTO, Collection>,
                                        private val platformDTOMapper: DTOModelMapper<PlatformDTO, Platform>,
                                        private val imagesDTOMapper: DTOModelMapper<ImageDTO, GameImage>) : DTOModelMapper<GameDTO, Game> {

    override fun mapFromDTO(dto: GameDTO): Game {
        with(dto) {
            val images = screenshots?.let {
                mapListOf(it, imagesDTOMapper)
            }
            val developers = developers?.let {
                mapListOf(it, companyDTOMapper)
            }
            val publishers = publishers?.let {
                mapListOf(it, companyDTOMapper)
            }
            val genres = genres?.let {
                mapListOf(it, genreDTOMapper)
            }
            val platforms = platforms?.let {
                mapListOf(it, platformDTOMapper)
            }

            return Game(id, name, created_at, updated_at, summary, storyline, url, rating,
                    rating_count, aggregated_rating, aggregated_rating_count, total_rating,
                    total_rating_count, first_release_date, mapCoverModel(cover),
                    mapTimeToBeatModel(time_to_beat), developers, publishers, genres,
                    collection?.let { collectionDTOMapper.mapFromDTO(it) }, System.currentTimeMillis(),
                    platforms, images)
        }
    }

    private fun mapTimeToBeatModel(type: TimeToBeatDTO?): TimeToBeat? {
        type?.apply {
            return TimeToBeat(hastly, normally, completely)
        }
        return null
    }

    private fun mapCoverModel(type: ImageDTO?): Cover? {
        type?.apply {
            return Cover(url, width, height)
        }
        return null
    }

    fun <T, E> mapListOf(list: List<E>, mapper: DTOModelMapper<E, T>): List<T> {
        val items: MutableList<T> = mutableListOf()
        list.forEach {
            items.add(mapper.mapFromDTO(it))
        }
        return items
    }
}