package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.BuildConfig
import com.piticlistudio.playednext.data.entity.net.GameRemote
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the Games API.
 * Created by jorge on 14/09/17.
 */
interface GameService {

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/games/")
    fun search(@Query("offset") offset: Int,
               @Query("search") query: String,
               @Query("fields") fields: String,
               @Query("limit") limit: Int): Single<List<GameRemote>>

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/games/{id}/")
    fun load(@Path("id") id: Int, @Query("fields") fields: String): Single<List<GameRemote>>
}