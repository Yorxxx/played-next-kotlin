package com.piticlistudio.playednext.data.repository.datasource.dao.game

import android.arch.persistence.room.EmptyResultSetException
import android.database.sqlite.SQLiteConstraintException
import com.piticlistudio.playednext.data.entity.dao.GameWithRelationalData
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named

class GameLocalImpl @Inject constructor(private val dao: GameDaoService,
                                        @Named("dao") private val companydao: CompanyDatasourceRepository,
                                        private val mapper: DaoModelMapper<GameWithRelationalData, Game>) : GameDatasourceRepository {

    override fun load(id: Int): Flowable<Game> {
        return dao.loadById(id.toLong())
                .distinctUntilChanged()
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .flatMap {
                    val game = it
                    Flowable.fromIterable(it.companyIdList)
                            .flatMapSingle { companydao.load(it.companyId) }
                            .toList()
                            .map { mapper.mapFromDao(game).apply {
                                developers = it.toList()
                            }}.toFlowable()
                }
    }

    override fun search(query: String, offset: Int, limit: Int): Flowable<List<Game>> {
        return dao.findByName(query)
                .distinctUntilChanged()
                .map {
                    val data = mutableListOf<Game>()
                    //it.forEach { data.add(mapper.mapFromEntity(it)) }
                    data
                }
    }

    override fun save(domainModel: Game): Completable {
        return Completable.defer {
            mapper.mapIntoDao(domainModel).also {
                /*try {
                    dao.insert(it)
                } catch (e: SQLiteConstraintException) {
                    dao.update(it)
                }*/
            }
            Completable.complete()
        }
    }
}