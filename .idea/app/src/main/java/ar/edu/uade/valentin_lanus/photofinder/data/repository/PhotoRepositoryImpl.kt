package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.api.ApiService
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo

class PhotoRepositoryImpl(
    private val apiService: ApiService,
    private val clientId: String
) : PhotoRepository {

    override suspend fun getRandomPhotos(count: Int): List<Photo> {
        return apiService.getRandomPhotos(count = count, clientId = clientId)
    }

    override suspend fun searchPhotos(query: String): List<Photo> {
        return apiService.searchPhotos(query = query, clientId = clientId).results
    }
}