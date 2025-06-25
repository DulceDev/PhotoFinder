package ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.data.local.LikedPhotoDao
import ar.edu.uade.valentin_lanus.photofinder.data.local.toEntity
import ar.edu.uade.valentin_lanus.photofinder.data.local.toPhoto
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val dao: LikedPhotoDao) : ViewModel() {


    private val _likedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val likedPhotos: StateFlow<List<Photo>> = _likedPhotos

    init {
        loadLikedPhotos()
    }

    fun addPhoto(photo: Photo) {
        viewModelScope.launch {
            dao.insert(photo.toEntity())
            loadLikedPhotos()
        }
    }

    fun removePhoto(photo: Photo) {
        viewModelScope.launch {
            dao.delete(photo.id)
            loadLikedPhotos()
        }
    }

    fun loadLikedPhotos() {
        viewModelScope.launch {
            _likedPhotos.value = dao.getAllLikedPhotos().map { it.toPhoto() }
        }
    }

    suspend fun isPhotoLiked(photoId: String): Boolean {
        return dao.isLiked(photoId)
    }
}
