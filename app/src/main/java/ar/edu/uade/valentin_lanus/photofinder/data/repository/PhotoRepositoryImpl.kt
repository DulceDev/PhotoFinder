package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.api.ApiService
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse
import ar.edu.uade.valentin_lanus.photofinder.data.local.LikedPhotoDao
import ar.edu.uade.valentin_lanus.photofinder.data.local.toPhoto
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo


class PhotoRepositoryImpl(
    private val apiService: ApiService,
    private val clientId: String,
    private val likedPhotoDao: LikedPhotoDao
) : PhotoRepository {
    override suspend fun searchPhotos(query: String, page: Int, perPage: Int): SearchResponse {
        return apiService.searchPhotos(
            query = query,
            perpage = perPage,
            page = page,
            clientId = clientId
        )
    }

    override suspend fun getLikedPhotos(): List<Photo> {
        return likedPhotoDao.getAllLikedPhotos().map { it.toPhoto() }
    }

}