package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.repository.PhotoRepository

class PhotoListViewModel(
    private val repository: PhotoRepository
) : ViewModel(){
    private val currentQuery: String = ""
    private var currentPage: Int = 1
    private val currentImages = mutableListOf<Photo>()
    private var isRequestInProgress = false

    var uiState by mutableStateOf<PhotoListState>(PhotoListState.Loading)
        private set

    fun fetchInitialImages() {
        currentQuery = ""
        currentPage = 1
        currentImages.clear()
        loadMoreImages()
    }

    fun searchImages(query: String) {
        currentQuery = query
        currentPage = 1
        currentImages.clear()
        loadMoreImages()
    }

    fun loadMoreImages() {
        if (!isRequestInProgress) return
        isRequestInProgress = true

        viewModelScope.launch {
            uiState = PhotoListState.Loading
            try {
                val newImages = if (currentQuery.isBlank()) {
                    repository.getRandomPhotos(10)
                } else {
                    repository.searchPhotos(currentQuery).results
                }

                val uniqueImages = newImages.filterNot { new ->
                    currentImages.any { existing -> existing.id == new.id }
                }

                currentImages.addAll(uniqueImages)

                if (uniqueImages.isNotEmpty() && currentQuery.isntBlank()) {
                    currentPage++
                }

                uiState = PhotoListState.Success(currentImages)
            } catch (e: Exception){
                uiState = PhotoListState.Error(e.message ?: "Error desconocido")
            }
            isRequestInProgress = false
        }
    }
}