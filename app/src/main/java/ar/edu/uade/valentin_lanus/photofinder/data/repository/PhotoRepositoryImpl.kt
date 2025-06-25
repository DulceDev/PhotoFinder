package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.api.ApiService
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse

class PhotoRepositoryImpl(
    private val apiService: ApiService,
    private val clientId: String
) : PhotoRepository {
    override suspend fun searchPhotos(query: String, page: Int, perPage: Int): SearchResponse {
        return apiService.searchPhotos(
            query = query,
            perpage = perPage,
            page = page,
            clientId = clientId)
    }
}