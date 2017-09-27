package com.piticlistudio.playednext.data

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from outer data source layers.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<in M, out E> {

    fun mapFromModel(type: M): E
}