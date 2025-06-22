package ar.edu.uade.valentin_lanus.photofinder.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse

interface ApiService {
    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("count") count: Int = 5,
        @Query("client_id") clientId: String
    ): List<Photo>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perpage: Int = 5,
        @Query("client_id") clientId: String
    ): SearchResponse
}
