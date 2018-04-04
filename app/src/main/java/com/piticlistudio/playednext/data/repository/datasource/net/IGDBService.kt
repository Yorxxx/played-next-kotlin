package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.BuildConfig
import com.piticlistudio.playednext.data.entity.igdb.IGDBCollection
import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.igdb.IGDBPlatform
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the Games API.
 * Created by jorge on 14/09/17.
 */
interface IGDBService {

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/games/")
    fun searchGames(@Query("offset") offset: Int,
                    @Query("search") query: String,
                    @Query("fields") fields: String = "id,name,slug,url,summary,collection,franchise,rating,storyline,popularity,total_rating,total_rating_count,rating_count,screenshots,cover,updated_at,created_at",
                    @Query("limit") limit: Int): Single<List<GameDTO>>

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/games/{id}/")
    fun loadGame(@Path("id") id: Int,
                 @Query("fields") fields: String = "id,name,slug,url,summary,collection,franchise,rating,storyline,popularity,total_rating,total_rating_count,rating_count,developers,publishers,genres,platforms,screenshots,cover,updated_at,created_at",
                 @Query("expand") expand: String? = "developers,publishers,genres,platforms,collection"): Single<List<GameDTO>>

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/collections/{id}/")
    fun loadCollection(@Path("id") id: Int, @Query("fields") fields: String = "id,name,logo,slug,created_at,updated_at"): Single<List<IGDBCollection>>

    @Headers("Accept: application/json", "user-key: " + BuildConfig.IGDB_API_KEY)
    @GET("/platforms/{id}/")
    fun loadPlatform(@Path("id") id: Int, @Query("fields") fields: String = "id,name,logo,slug,created_at,updated_at"): Single<List<IGDBPlatform>>
}