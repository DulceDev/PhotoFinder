package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.data.local.LikedPhotoDao
import ar.edu.uade.valentin_lanus.photofinder.data.local.toEntity
import ar.edu.uade.valentin_lanus.photofinder.data.local.toPhoto
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoDetailViewModel(
    private val dao: LikedPhotoDao
) : ViewModel() {

    private val _photo = MutableStateFlow<Photo?>(null)
    val photo: StateFlow<Photo?> = _photo.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    fun setPhoto(photo: Photo) {
        _photo.value = photo
        viewModelScope.launch {
            _isLiked.value = dao.isLiked(photo.id)
        }
    }

    suspend fun toggleLike(): Boolean {
        val currentPhoto = _photo.value ?: return false

        val likedNow = !_isLiked.value
        if (likedNow) {
            dao.insert(currentPhoto.toEntity())
        } else {
            dao.delete(currentPhoto.id)
        }

        _isLiked.value = likedNow
        return likedNow
    }

    suspend fun loadPhotoById(photoId: String, sources: List<Photo>) {
        val fromMemory = sources.find { it.id == photoId }
        if (fromMemory != null) {
            setPhoto(fromMemory)
        } else {
            val fromDb = dao.getById(photoId)
            if (fromDb != null) {
                setPhoto(fromDb.toPhoto())
            } else {
                _photo.value = null
                _isLiked.value = false
            }
        }
    }
}