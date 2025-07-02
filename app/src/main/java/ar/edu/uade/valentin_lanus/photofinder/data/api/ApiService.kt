package ar.edu.uade.valentin_lanus.photofinder.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse

interface ApiService {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perpage: Int,
        @Query("page") page: Int,
        @Query("client_id") clientId: String
    ): SearchResponse
}