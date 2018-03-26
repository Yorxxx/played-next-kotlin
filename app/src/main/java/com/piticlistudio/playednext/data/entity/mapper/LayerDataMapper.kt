package com.piticlistudio.playednext.data.entity.mapper

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from outer data source layers.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface LayerDataMapper<M, E> {

    fun mapFromModel(type: M): E
    fun mapFromEntity(type: E): M
}

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