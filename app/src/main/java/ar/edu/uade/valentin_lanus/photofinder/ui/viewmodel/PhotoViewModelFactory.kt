package ar.edu.uade.valentin_lanus.photofinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.valentin_lanus.photofinder.data.repository.PhotoRepository
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel

class PhotoViewModelFactory(
    private val repository: PhotoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}