package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.data.local.LikedPhotoDao
import ar.edu.uade.valentin_lanus.photofinder.data.local.toEntity
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val dao: LikedPhotoDao) : ViewModel() {
    private val _photo = MutableStateFlow<Photo?>(null)
    val photo = _photo.asStateFlow()

    val isLiked: StateFlow<Boolean> = _photo
        .filterNotNull()
        .flatMapLatest { dao.isLiked(it.id) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setPhoto(photo: Photo) = _photo.tryEmit(photo)

    fun toggleLike() = viewModelScope.launch {
        _photo.value?.let {
            if (isLiked.value) dao.delete(it.id)
            else dao.insert(it.toEntity())
        }
    }
}