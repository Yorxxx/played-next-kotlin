package com.piticlistudio.playednext.data.repository.datasource.giantbomb

import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombEntityResponse
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGame
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the Giantbomb API.
 * Created by e-jegi on 3/26/2018.
 */
interface GiantbombService {

    @Headers("Accept: application/json")
    @GET("search/")
    fun searchGames(@Query("api_key") apiKey: String = "77bd20a4ab9c793aea91ed7d0bbfa1b46bbeef61",
                    @Query("query") query: String,
                    @Query("resources") resources: String = "game",
                    @Query("format") format: String = "json",
                    @Query("field_list") fields: String = SEARCH_FIELD_LIST,
                    @Query("page") page: Int = 1,
                    @Query("limit") limit: Int = 10): Single<GiantbombListResponse<GiantbombGame>>

    @Headers("Accept: application/json")
    @GET("game/{id}/")
    fun fetchGame(@Path("id") id: Int,
                  @Query("api_key") apiKey: String = "77bd20a4ab9c793aea91ed7d0bbfa1b46bbeef61",
                  @Query("format") format: String = "json",
                  @Query("field_list") fields: String = FETCH_FIELD_LIST): Single<GiantbombEntityResponse<GiantbombGame>>
}

private const val SEARCH_FIELD_LIST = "id,image,name,platforms,date_added,date_last_updated"
private const val FETCH_FIELD_LIST = "date_added,date_last_updated,deck,description,expected_release_day,expected_release_month,expected_release_quarter,expected_release_year,id,image,name,original_release_date,platforms,images,developers,publishers,franchises,genres"