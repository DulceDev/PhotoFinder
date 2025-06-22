package ar.edu.uade.valentin_lanus.photofinder.data.repository

import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo

interface PhotoRepository {
    suspend fun getRandomPhotos(count: Int): List<Photo>
    suspend fun searchPhotos(query: String): List<Photo>
}