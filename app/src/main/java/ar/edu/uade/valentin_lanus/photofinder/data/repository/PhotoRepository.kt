package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse

interface PhotoRepository {
    suspend fun searchPhotos(query: String, page: Int, perPage: Int): SearchResponse

    suspend fun getLikedPhotos(): List<Photo>
}