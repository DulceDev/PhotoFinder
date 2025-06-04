package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.valentin_lanus.photofinder.Secrets
import kotlinx.coroutines.launch
import ar.edu.uade.valentin_lanus.photofinder.data.api.RetrofitInstance
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo

class PhotoListViewModel : ViewModel(){
    var images by mutableStateOf<List<Photo>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    private var currentQuery: String = ""

    fun fetchInitialImages(){
        images = emptyList()
        currentQuery = ""
        loadMoreImages()
    }

    fun searchImages(query: String){
        images = emptyList()
        currentQuery = query
        loadMoreImages()
    }

    fun loadMoreImages(){ // Carga de imagenes de 2 en 2 (infinito)
        viewModelScope.launch {
            isLoading = true
            try {
                val newImages = if (currentQuery.isBlank()) {
                    RetrofitInstance.api.getRandomPhotos(count = 2, clientId = Secrets.unsplash_api) // Obetengo las fotos de la api
                } else {
                    val result = RetrofitInstance.api.searchPhotos(
                        query = currentQuery,
                        perpage = 2,
                        clientId = Secrets.unsplash_api
                    )
                    result.results
                }
                images = images + newImages
            } catch (e: Exception){
                Log.e("ImageVM", "Error: ${e.message}")
            }
            isLoading = false
        }
    }
}