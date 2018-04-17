package com.piticlistudio.playednext.data.entity.mapper

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from data layer source into domain layer.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface DataLayerMapper<I, O> {

    fun mapFromDataLayer(model: I): O
}

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from domain layer source into data layer.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface DomainLayerMapper<I, O> {

    fun mapIntoDataLayerModel(model: I): O
}