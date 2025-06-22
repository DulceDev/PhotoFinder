package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.model.SearchResponse

interface PhotoRepository {
    suspend fun getRandomPhotos(count: Int): List<Photo>
    suspend fun searchPhotos(query: String): SearchResponse

}