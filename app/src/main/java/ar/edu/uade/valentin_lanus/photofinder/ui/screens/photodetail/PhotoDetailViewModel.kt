package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.data.local.LikedPhotoDao
import ar.edu.uade.valentin_lanus.photofinder.data.local.toEntity
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

    fun toggleLike() = viewModelScope.launch {
        _photo.value?.let {
            if (isLiked.value) {
                dao.delete(it.id)
                _isLiked.value = false
            } else {
                dao.insert(it.toEntity())
                _isLiked.value = true
            }
        }
    }
}