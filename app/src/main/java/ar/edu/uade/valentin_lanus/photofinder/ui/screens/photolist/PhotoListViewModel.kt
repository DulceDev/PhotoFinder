package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.repository.PhotoRepository
import kotlinx.coroutines.launch

class PhotoListViewModel(
    private val repository: PhotoRepository
) : ViewModel() {

    private var currentQuery: String = ""
    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    private var isRequestInProgress = false
    private var endReached = false

    private val pageSize = 30

    private var currentImages = mutableListOf<Photo>()
    private val imageIds = mutableSetOf<String>()

    private val fallbackQueries = listOf("naturaleza", "flore", "autos", "aviones", "animales", "paisajes")
    private var lastRandomQuery = fallbackQueries.random()

    var uiState by mutableStateOf<PhotoListState>(PhotoListState.Loading)
        private set


    fun isLoading(): Boolean = isRequestInProgress
    fun isEndReached(): Boolean = endReached

    fun fetchInitialImages() {
        currentQuery = ""
        currentPage = 1
        totalPages = Int.MAX_VALUE
        endReached = false
        currentImages.clear()
        imageIds.clear()
        lastRandomQuery = fallbackQueries.random()
        loadImages()
    }

    fun searchImages(query: String) {
        currentQuery = query
        currentPage = 1
        totalPages = Int.MAX_VALUE
        endReached = false
        currentImages.clear()
        imageIds.clear()
        loadImages()
    }

    fun loadImages() {
        if (isRequestInProgress || endReached || currentPage > totalPages) return
        isRequestInProgress = true

        viewModelScope.launch {
            if(currentImages.isEmpty()){
                uiState = PhotoListState.Loading
            }

            try {
                val queryToUse = if (currentQuery.isNotBlank()) currentQuery else lastRandomQuery

                val response = repository.searchPhotos(
                    query = queryToUse,
                    page = currentPage,
                    perPage = pageSize
                )

                val uniqueImages = response.results.filter { it.id !in imageIds }

                if (uniqueImages.isEmpty() || currentPage >= response.total_pages) {
                    endReached = true
                }

                currentImages.addAll(uniqueImages)
                imageIds.addAll(uniqueImages.map { it.id })

                totalPages = response.total_pages
                currentPage++

                uiState = PhotoListState.Success(currentImages)

            } catch (e: Exception) {
                uiState = PhotoListState.Error("Error: ${e.localizedMessage}")
            }
            isRequestInProgress = false
        }
    }
}