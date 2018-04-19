package com.piticlistudio.playednext.ui

/**
 * Interface for model mappers. It provide helper methods that facilitate retrieving of models
 * from domain layer source into data layer.
 *
 * @param <M> the data model input type
 * @param <E> the entity model output type
 */
interface PresentationLayerMapper<I, O> {

    fun mapIntoPresentationLayerModel(model: I): O
}