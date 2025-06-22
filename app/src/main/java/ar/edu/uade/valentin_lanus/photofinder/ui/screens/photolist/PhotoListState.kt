package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist

import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo

sealed class PhotoListState{
    object Loading : PhotoListState()
    data class Success(val photos: List<Photo>) : PhotoListState()
    data class Error(val message: String) : PhotoListState()
}