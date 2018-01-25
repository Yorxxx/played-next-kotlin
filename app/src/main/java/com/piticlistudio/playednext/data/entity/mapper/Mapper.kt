package com.piticlistudio.playednext.data.entity.mapper

/**
 * Mapper between DAO models into entity models
 */
interface DaoModelMapper<M, E> {

    fun mapFromDao(dao: M): E
    fun mapIntoDao(entity: E): M
}

/**
 * Maps DTO models into entity.
 */
interface DTOModelMapper<M, E> {
    fun mapFromDTO(dto: M): E
}