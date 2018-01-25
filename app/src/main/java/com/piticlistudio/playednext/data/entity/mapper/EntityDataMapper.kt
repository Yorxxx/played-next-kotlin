package com.piticlistudio.playednext.data.entity.mapper

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from outer data source layers.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface EntityDataMapper<M, E> {

    fun mapFromEntity(type: E): M
}